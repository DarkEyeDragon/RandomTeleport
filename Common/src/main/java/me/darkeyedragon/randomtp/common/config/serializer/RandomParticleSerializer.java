package me.darkeyedragon.randomtp.common.config.serializer;

import me.darkeyedragon.randomtp.api.world.RandomParticle;
import me.darkeyedragon.randomtp.common.world.CommonParticle;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Objects;

public class RandomParticleSerializer implements TypeSerializer<RandomParticle> {

    public static final RandomParticleSerializer INSTANCE = new RandomParticleSerializer();
    private static final String PARTICLE = "particle";

    @Override
    public RandomParticle deserialize(Type source, ConfigurationNode node) throws SerializationException {
        String particleStr = node.getString();
        if (particleStr == null) {
            throw new SerializationException("Particle string is null");
        }
        if (particleStr.equalsIgnoreCase("none")) return RandomParticle.NONE;
        final String[] particleString = Objects.requireNonNull(node.getString()).split(":");
        if (particleString[0].equalsIgnoreCase("none")) return new CommonParticle("NONE", 0);
        return new CommonParticle(particleString[0], Integer.parseInt(particleString[1]));
    }

    @Override
    public void serialize(Type type, @Nullable RandomParticle particle, ConfigurationNode target) throws SerializationException {
        if (particle == null) {
            target.raw(null);
            return;
        }
        target.node(PARTICLE).set(particle.toString());
    }
}
