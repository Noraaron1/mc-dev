package net.minecraft.server;

public class EntityMinecartTNT extends EntityMinecartAbstract {

    private int fuse = -1;

    public EntityMinecartTNT(World world) {
        super(world);
    }

    public EntityMinecartTNT(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public int getType() {
        return 3;
    }

    public Block n() {
        return Block.TNT;
    }

    public void l_() {
        super.l_();
        if (this.fuse > 0) {
            --this.fuse;
            this.world.addParticle("smoke", this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D);
        } else if (this.fuse == 0) {
            this.c(this.motX * this.motX + this.motZ * this.motZ);
        }

        if (this.positionChanged) {
            double d0 = this.motX * this.motX + this.motZ * this.motZ;

            if (d0 >= 0.009999999776482582D) {
                this.c(d0);
            }
        }
    }

    public void a(DamageSource damagesource) {
        super.a(damagesource);
        double d0 = this.motX * this.motX + this.motZ * this.motZ;

        if (!damagesource.c()) {
            this.a(new ItemStack(Block.TNT, 1), 0.0F);
        }

        if (damagesource.m() || damagesource.c() || d0 >= 0.009999999776482582D) {
            this.c(d0);
        }
    }

    protected void c(double d0) {
        if (!this.world.isStatic) {
            double d1 = Math.sqrt(d0);

            if (d1 > 5.0D) {
                d1 = 5.0D;
            }

            this.world.explode(this, this.locX, this.locY, this.locZ, (float) (4.0D + this.random.nextDouble() * 1.5D * d1), true);
            this.die();
        }
    }

    protected void b(float f) {
        if (f >= 3.0F) {
            float f1 = f / 10.0F;

            this.c((double) (f1 * f1));
        }

        super.b(f);
    }

    public void a(int i, int j, int k, boolean flag) {
        if (flag && this.fuse < 0) {
            this.d();
        }
    }

    public void d() {
        this.fuse = 80;
        if (!this.world.isStatic) {
            this.world.broadcastEntityEffect(this, (byte) 10);
            this.world.makeSound(this, "random.fuse", 1.0F, 1.0F);
        }
    }

    public boolean u() {
        return this.fuse > -1;
    }

    public float a(Explosion explosion, World world, int i, int j, int k, Block block) {
        return this.u() && (BlockMinecartTrackAbstract.e_(block.id) || BlockMinecartTrackAbstract.d_(world, i, j + 1, k)) ? 0.0F : super.a(explosion, world, i, j, k, block);
    }

    public boolean a(Explosion explosion, World world, int i, int j, int k, int l, float f) {
        return this.u() && (BlockMinecartTrackAbstract.e_(l) || BlockMinecartTrackAbstract.d_(world, i, j + 1, k)) ? false : super.a(explosion, world, i, j, k, l, f);
    }

    protected void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKey("TNTFuse")) {
            this.fuse = nbttagcompound.getInt("TNTFuse");
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("TNTFuse", this.fuse);
    }
}
