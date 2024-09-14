package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.AudioSystem.SoundPlayerComponent;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.ParticleSystem.ParticleSystemComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.BoxColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.CircleColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.CylinderColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.SimpleCharacterController;

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
        types.add(SimpleCharacterController.class);
        types.add(CameraControlComponent.class);
        types.add(SoundPlayerComponent.class);
        types.add(ParticleSystemComponent.class);
    }

    public static final List<Class<? extends Component>> types = new ArrayList<>();

    public static void addComponentType(Class<? extends Component> TYPE)
    {
        types.add(TYPE);
    }
}