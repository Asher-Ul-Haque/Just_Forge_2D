package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.AudioSystem.SoundPlayerComponent;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.ParticleSystem.ParticleSystemComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.*;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints.HingeComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints.PistonComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints.SpringComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.KeyboardControllerComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.TextSystem.TextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentList
{
    private static final List<Class<? extends Component>> types = new ArrayList<>();
    private static final Map<Class<? extends Component>, ComponentRegistry> registry = new HashMap<>();

    public static void initialize()
    {
        registerComponent(TransformComponent.class, Icons.ExpandArrowsAlt + "  Transform", null);
        registerComponent(SpriteComponent.class, Icons.Image + "  Sprite", List.of(TransformComponent.class));

        registerComponent(WindowManagerComponent.class, Icons.WindowMaximize + "  Window Manager", null);
        registerComponent(RigidBodyComponent.class, Icons.Weight + "  Physics", List.of(TransformComponent.class));
        registerComponent(CircleColliderComponent.class, Icons.Circle + "  Circle Collider", List.of(RigidBodyComponent.class));
        registerComponent(BoxColliderComponent.class, Icons.Square + "  Box Collider", List.of(RigidBodyComponent.class));
        registerComponent(CylinderColliderComponent.class, Icons.Pills + "  Cylinder Collider", List.of(RigidBodyComponent.class));
        registerComponent(PolygonColliderComponent.class, Icons.DrawPolygon + "  Polygon Collider", List.of(RigidBodyComponent.class));
        registerComponent(EdgeColliderComponent.class, Icons.Underline + "  Edge Collider", List.of(RigidBodyComponent.class));

        registerComponent(KeyboardControllerComponent.class, Icons.Keyboard + "  Keyboard Controller", List.of(TransformComponent.class));
        registerComponent(MouseControlComponent.class, Icons.Mouse + "  Mouse Controller", List.of(TransformComponent.class));
        registerComponent(CameraControlComponent.class, Icons.Camera + "  Camera Controller", List.of(TransformComponent.class));

        registerComponent(AnimationComponent.class, Icons.Film + "  Animation", List.of(SpriteComponent.class));
        registerComponent(SoundPlayerComponent.class, Icons.Music + "  Sound Player", null);
        registerComponent(ParticleSystemComponent.class, Icons.Braille + "  Particle System", List.of(SpriteComponent.class));
        registerComponent(TextComponent.class, Icons.EnvelopeOpenText + "  Text", List.of(TransformComponent.class));

        registerComponent(SpringComponent.class, Icons.Icons + "  Spring", List.of(RigidBodyComponent.class));
        registerComponent(HingeComponent.class, Icons.Icons + "  Hinge", List.of(RigidBodyComponent.class));
        registerComponent(PistonComponent.class, Icons.Icons + "  Piston", List.of(RigidBodyComponent.class));
    }

    private static void registerComponent(Class<? extends Component> type, String name, List<Class<? extends Component>> requiredComponents)
    {
        if (!hasComponentType(type))
        {
            types.add(type);
            registry.put(type, new ComponentRegistry(name, requiredComponents));
        }
    }

    public static List<Class<? extends Component>> getTypes()
    {
        return new ArrayList<>(types);
    }

    public static boolean hasComponentType(Class<? extends Component> type)
    {
        return types.contains(type);
    }

    public static ComponentRegistry getComponentInfo(Class<? extends Component> type)
    {
        return registry.get(type);
    }

    // - - - Utility class for holding component metadata
    public record ComponentRegistry(String name, List<Class<? extends Component>> requiredComponents){};
}
