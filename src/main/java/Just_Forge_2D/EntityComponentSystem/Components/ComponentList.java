package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.AudioSystem.SoundPlayerComponent;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.ParticleSystem.ParticleSystemComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.*;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints.DistanceJoint;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.KeyboardControllerComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;

import java.util.ArrayList;
import java.util.List;

public class ComponentList
{
    public static void initialize()
    {
        types.add(TransformComponent.class);
        types.add(SpriteComponent.class);
        types.add(RigidBodyComponent.class);
        types.add(CircleColliderComponent.class);
        types.add(BoxColliderComponent.class);
        types.add(CylinderColliderComponent.class);
        types.add(PolygonColliderComponent.class);
        types.add(EdgeColliderComponent.class);
        types.add(KeyboardControllerComponent.class);
        types.add(MouseControlComponent.class);
        types.add(CameraControlComponent.class);
        types.add(SoundPlayerComponent.class);
        types.add(ParticleSystemComponent.class);
        types.add(DistanceJoint.class);
    }

    public static final List<Class<? extends Component>> types = new ArrayList<>();

    public static void addComponentType(Class<? extends Component> TYPE)
    {
        types.add(TYPE);
    }

    public static void removeComponentType(Class<? extends Component> TYPE)
    {
        types.remove(TYPE);
    }

    public static boolean hasComponentType(Class<? extends Component> TYPE)
    {
        return types.contains(TYPE);
    }
}