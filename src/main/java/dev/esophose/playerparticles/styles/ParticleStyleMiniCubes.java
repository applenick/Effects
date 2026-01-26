package dev.esophose.playerparticles.styles;

import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticleStyleMiniCubes extends ConfiguredParticleStyle {

    private int step = 0;

    private int numCubes;
    private double cubeSize;
    private double spacing;
    private double floatingHeight;

    protected ParticleStyleMiniCubes() {
        super("minicubes", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        // Generate multiple small cubes around the central location
        for (int i = 0; i < this.numCubes; i++) {
            double angle = (this.step + i * (360.0 / this.numCubes)) % 360;
            double offsetX = MathL.cos(Math.toRadians(angle)) * this.spacing;
            double offsetZ = MathL.sin(Math.toRadians(angle)) * this.spacing;
            Location cubeCenter = location.clone().add(offsetX, this.floatingHeight, offsetZ);
            particles.addAll(createCubeParticles(cubeCenter));
        }

        return particles;
    }

    private List<PParticle> createCubeParticles(Location center) {
        List<PParticle> cubeParticles = new ArrayList<>();
        double halfSize = this.cubeSize / 2.0;

        for (double x = -halfSize; x <= halfSize; x += halfSize) {
            for (double y = -halfSize; y <= halfSize; y += halfSize) {
                for (double z = -halfSize; z <= halfSize; z += halfSize) {
                    cubeParticles.add(PParticle.point(center.clone().add(x, y, z)));
                }
            }
        }

        for (double t = -halfSize; t <= halfSize; t += 0.2) {
            cubeParticles.add(PParticle.point(center.clone().add(t, -halfSize, -halfSize)));
            cubeParticles.add(PParticle.point(center.clone().add(t, halfSize, halfSize)));
            cubeParticles.add(PParticle.point(center.clone().add(t, -halfSize, halfSize)));
            cubeParticles.add(PParticle.point(center.clone().add(t, halfSize, -halfSize)));

            cubeParticles.add(PParticle.point(center.clone().add(-halfSize, t, -halfSize)));
            cubeParticles.add(PParticle.point(center.clone().add(halfSize, t, halfSize)));
            cubeParticles.add(PParticle.point(center.clone().add(-halfSize, t, halfSize)));
            cubeParticles.add(PParticle.point(center.clone().add(halfSize, t, -halfSize)));

            cubeParticles.add(PParticle.point(center.clone().add(-halfSize, -halfSize, t)));
            cubeParticles.add(PParticle.point(center.clone().add(halfSize, halfSize, t)));
            cubeParticles.add(PParticle.point(center.clone().add(-halfSize, halfSize, t)));
            cubeParticles.add(PParticle.point(center.clone().add(halfSize, -halfSize, t)));
        }

        return cubeParticles;
    }

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % 360;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("GLASS", "GLASS_PANE");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("num-cubes", 4, "The number of floating cubes around the player");
        this.setIfNotExists("cube-size", 0.5, "The size of each floating cube");
        this.setIfNotExists("spacing", 1.5, "The spacing between the cubes");
        this.setIfNotExists("floating-height", 1.0, "The height at which the cubes float above the ground");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.numCubes = config.getInt("num-cubes");
        this.cubeSize = config.getDouble("cube-size");
        this.spacing = config.getDouble("spacing");
        this.floatingHeight = config.getDouble("floating-height");
    }

}