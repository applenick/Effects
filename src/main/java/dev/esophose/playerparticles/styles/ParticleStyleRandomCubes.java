package dev.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;

import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;

public class ParticleStyleRandomCubes extends ConfiguredParticleStyle {

    private static final int TICKS_PER_SECOND = 20;

    private List<Cube> cubes;
    private int cubeLifetimeTicks;
    private int maxCubes;
    private double cubeSize;
    private double spawnRadius;
    private Random random;

    protected ParticleStyleRandomCubes() {
        super("randomcubes", true, true, 0);
        this.cubes = new ArrayList<>();
        this.random = new Random();
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        Iterator<Cube> iterator = this.cubes.iterator();
        while (iterator.hasNext()) {
            Cube cube = iterator.next();
            if (cube.age >= this.cubeLifetimeTicks) {
                iterator.remove();
            } else {
                cube.age++;
                particles.addAll(createCubeParticles(cube.location, cubeSize));
            }
        }

        if (this.cubes.size() < this.maxCubes) {
            Location randomLocation = getRandomLocationAround(location, this.spawnRadius);
            this.cubes.add(new Cube(randomLocation));
        }

        return particles;
    }

    private List<PParticle> createCubeParticles(Location center, double size) {
        List<PParticle> cubeParticles = new ArrayList<>();
        double halfSize = size / 2.0;
        double step = size / 4.0; 

        for (double x = -halfSize; x <= halfSize; x += step) {
            for (double y = -halfSize; y <= halfSize; y += step) {
                for (double z = -halfSize; z <= halfSize; z += step) {
                    int edges = 0;
                    if (Math.abs(x) == halfSize) edges++;
                    if (Math.abs(y) == halfSize) edges++;
                    if (Math.abs(z) == halfSize) edges++;
                    if (edges >= 2) { 
                        cubeParticles.add(PParticle.point(center.clone().add(x, y, z)));
                    }
                }
            }
        }

        return cubeParticles;
    }

    private Location getRandomLocationAround(Location center, double radius) {
        double angle = random.nextDouble() * 2 * Math.PI;
        double distance = random.nextDouble() * radius;
        double xOffset = Math.cos(angle) * distance;
        double zOffset = Math.sin(angle) * distance;
        double yOffset = (random.nextDouble() - 0.5) * radius; // Random height offset

        return center.clone().add(xOffset, yOffset, zOffset);
    }

    @Override
    public void updateTimers() {
        // No-op
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("MELON");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("cube-lifetime", 20, "The lifetime of each cube in ticks (20 ticks = 1 second)");
        this.setIfNotExists("max-cubes", 3, "The maximum number of cubes at any time");
        this.setIfNotExists("cube-size", 0.5, "The size of each cube");
        this.setIfNotExists("spawn-radius", 2.0, "The radius around the player where cubes can spawn");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.cubeLifetimeTicks = config.getInt("cube-lifetime");
        this.maxCubes = config.getInt("max-cubes");
        this.cubeSize = config.getDouble("cube-size");
        this.spawnRadius = config.getDouble("spawn-radius");
    }

    private static class Cube {
        Location location;
        int age; // ticks

        Cube(Location location) {
            this.location = location;
            this.age = 0;
        }
    }

}
