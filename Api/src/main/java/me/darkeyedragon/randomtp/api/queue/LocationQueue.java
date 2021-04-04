package me.darkeyedragon.randomtp.api.queue;

import me.darkeyedragon.randomtp.api.config.section.subsection.SectionWorldDetail;
import me.darkeyedragon.randomtp.api.plugin.RandomTeleportPlugin;
import me.darkeyedragon.randomtp.api.world.location.RandomLocation;
import me.darkeyedragon.randomtp.api.world.location.search.LocationSearcher;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LocationQueue extends ObservableQueue<RandomLocation> {

    protected final int MAX_CONCURRENT = 5;
    private final RandomTeleportPlugin<?> plugin;
    private final LocationSearcher baseLocationSearcher;

    public LocationQueue(RandomTeleportPlugin<?> plugin, int capacity, LocationSearcher baseLocationSearcher) {
        super(capacity);
        this.plugin = plugin;
        this.baseLocationSearcher = baseLocationSearcher;
    }

    public void generate(SectionWorldDetail sectionWorldDetail) {
        generate(sectionWorldDetail, super.remainingCapacity());
    }

    /**
     * Generates locations based on the {@link SectionWorldDetail}.
     * To prevent the thread from choking a hard limit is placed on the loop. Limiting the amount of
     * searches that can be scheduled at once.
     *
     * @param sectionWorldDetail the {@link SectionWorldDetail}
     * @param amount             the amount of required locations to be found.
     * @author Trigary
     */
    public void generate(SectionWorldDetail sectionWorldDetail, int amount) {
        AtomicInteger startedAmount = new AtomicInteger();
        AtomicReference<Runnable> worker = new AtomicReference<>();
        worker.set(() -> baseLocationSearcher.getRandom(sectionWorldDetail).thenAccept(randomLocation -> {
            if (super.contains(randomLocation)) {
                worker.get().run();
                return;
            }
            offer(randomLocation);
            if (startedAmount.getAndIncrement() < amount) {
                worker.get().run();
            }
        }).exceptionally(throwable -> {
            plugin.getLogger().warn(throwable.getMessage());
            return null;
        }));
        int workersToStart = Math.min(amount, MAX_CONCURRENT);
        for (int workerIndex = 0; workerIndex < workersToStart; workerIndex++) {
            if (startedAmount.getAndIncrement() < amount) {
                worker.get().run();
            }
        }
    }
}