package Just_Forge_2D.ParticleSystem;

import org.jbox2d.particle.ParticleType;

public enum ParticleTypes
{
    WATER_PARTICLE(ParticleType.b2_waterParticle),
    ZOMBIE_PARTICLE(ParticleType.b2_zombieParticle),
    WALL_PARTICLE(ParticleType.b2_wallParticle),
    SPRING_PARTICLE(ParticleType.b2_springParticle),
    ELASTIC_PARTICLE(ParticleType.b2_elasticParticle),
    VISCOUS_PARTICLE(ParticleType.b2_viscousParticle),
    POWDER_PARTICLE(ParticleType.b2_powderParticle),
    TENSILE_PARTICLE(ParticleType.b2_tensileParticle),
    COLOR_MIXING_PARTICLE(ParticleType.b2_colorMixingParticle);

    public int mask = 0;

    ParticleTypes(int MASK)
    {
        this.mask = MASK;
    }
}
