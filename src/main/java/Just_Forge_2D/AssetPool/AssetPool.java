package Just_Forge_2D.AssetPool;

public interface AssetPool<T>
{
    void add(String NAME, Resource RESOURCE);
    T get(String NAME);
    void remove(String NAME);
    void clear();
}
