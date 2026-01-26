package dev.esophose.playerparticles.styles;

import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticleStylePulsingCubes extends ConfiguredParticleStyle {

    private int step = 0;
    private int cycleDuration;
    private double minCubeSize;
    private double maxCubeSize;
    private double cubeHeightAbovePlayer;

    protected ParticleStylePulsingCubes() {
        super("pulsingcube", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        double progress = (double) step / cycleDuration;
        double angle = progress * 2 * Math.PI; // 0 to 2PI
        double sizeMultiplier = 0.5 * (1 + Math.sin(angle)); // Varies between 0 and 1
        double currentSize = minCubeSize + sizeMultiplier * (maxCubeSize - minCubeSize);

        Location cubeLocation = location.clone().add(0, cubeHeightAbovePlayer, 0);

        particles.addAll(createCubeParticles(cubeLocation, currentSize));

        return particles;
    }

    private List<PParticle> createCubeParticles(Location center, double size) {
        List<PParticle> cubeParticles = new ArrayList<>();
        double halfSize = size / 2.0;
        double step = size / 5.0;

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

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % cycleDuration;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("REDSTONE_LAMP");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("min-cube-size", 0.5, "The minimum size of the cube");
        this.setIfNotExists("max-cube-size", 1.5, "The maximum size of the cube");
        this.setIfNotExists("cycle-duration", 40, "The number of ticks it takes to complete one expand-contract cycle");
        this.setIfNotExists("cube-height-above-player", 2.0, "The height above the player where the cube appears");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.minCubeSize = config.getDouble("min-cube-size");
        this.maxCubeSize = config.getDouble("max-cube-size");
        this.cycleDuration = config.getInt("cycle-duration");
        this.cubeHeightAbovePlayer = config.getDouble("cube-height-above-player");
    }

}
