package me.darkeyedragon.randomtp.common.config.serializer;

import me.darkeyedragon.randomtp.api.world.RandomParticle;
import me.darkeyedragon.randomtp.common.world.CommonParticle;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class RandomParticleSerializer implements TypeSerializer<RandomParticle> {

    public static final RandomParticleSerializer INSTANCE = new RandomParticleSerializer();
    private static final String PARTICLE = "particle";

    @Override
    public RandomParticle deserialize(Type source, ConfigurationNode node) throws SerializationException {
        //final ConfigurationNode particleNode = nonVirtualNode(node, PARTICLE);
        final String[] particleString = node.getString().split(":");
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
