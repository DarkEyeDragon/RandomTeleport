package me.darkeyedragon.randomtp.api.world;

public interface RandomParticle {

    RandomParticle NONE = new RandomParticle() {
        @Override
        public String getId() {
            return "NONE";
        }

        @Override
        public int getAmount() {
            return 0;
        }
    };

    RandomParticle DEFAULT = new RandomParticle() {
        @Override
        public String getId() {
            return "NONE";
        }

        @Override
        public int getAmount() {
            return 20;
        }
    };

    static RandomParticle NONE() {
        return new RandomParticle() {
            @Override
            public String getId() {
                return "NONE";
            }

            @Override
            public int getAmount() {
                return 0;
            }
        };
    }

    String getId();

    int getAmount();
}
