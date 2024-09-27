package Just_Forge_2D.PhysicsSystem.PhysicsManagers;

import Just_Forge_2D.Utils.Logger;

public class CollisionLayer
{
    // - - - private variables
    private short categoryBits;
    private short maskBits;


    // - - - | Functions | - - -


    // - - - Constructors - - -

    public CollisionLayer()
    {
        this.categoryBits = 0x0001;
        this.maskBits = (short) 0xFFFF;
    }

    public CollisionLayer(short CATEGORY_BITS, short MASK_BITS)
    {
        this.categoryBits = CATEGORY_BITS;
        this.maskBits = MASK_BITS;
    }


    // - - - Layer getter setterS - - -

    public void setLayer(int layer)
    {
        if (layer < 1 || layer > 16)
        {
            Logger.FORGE_LOG_ERROR("Layer must be between 1 and 16.");
            return;
        }
        this.categoryBits = (short) (1 << (layer - 1));
    }

    public int getLayer()
    {
        for (int i = 0; i < 16; i++)
        {
            if ((categoryBits & (1 << i)) != 0)
            {
                return i + 1;
            }
        }
        Logger.FORGE_LOG_FATAL("Physics System Fail: Collision Filtering");
        return -1;
    }

    public void setCollidableLayers(int... LAYERS)
    {
        this.maskBits = 0;
        for (int layer : LAYERS)
        {
            if (layer < 1 || layer > 16)
            {
                Logger.FORGE_LOG_ERROR("Layer must be between 1 and 16.");
                return;
            }
            this.maskBits |= (short) (1 << (layer - 1));
        }
    }

    public boolean canCollideWith(int LAYER)
    {
        if (LAYER < 1 || LAYER > 16)
        {
            Logger.FORGE_LOG_ERROR("Layer must be between 1 and 16.");
            return false;
        }
        return (maskBits & (1 << (LAYER - 1))) != 0;
    }

    public void setCollideWithLayer(int LAYER, boolean CAN_COLLIDE)
    {
        if (LAYER < 1 || LAYER > 16)
        {
            throw new IllegalArgumentException("Layer must be between 1 and 16.");
        }
        if (CAN_COLLIDE)
        {
            this.maskBits |= (short) (1 << (LAYER - 1));
        }
        else
        {
            this.maskBits &= (short) ~(1 << (LAYER - 1));
        }
    }


    // - - - Data getters - - -

    public short getCategoryBits()
    {
        return categoryBits;
    }

    public short getMaskBits()
    {
        return maskBits;
    }

    @Override
    public String toString()
    {
        return "CategoryBits: " + Integer.toHexString(categoryBits) + ", MaskBits: " + Integer.toHexString(maskBits);
    }
}