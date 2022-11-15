package com.github.wolfshotz.wyrmroost.registry;

import com.github.wolfshotz.wyrmroost.Wyrmroost;
import com.github.wolfshotz.wyrmroost.client.model.entity.RoostStalkerModel;
import com.github.wolfshotz.wyrmroost.client.render.entity.dragon.RoostStalkerRenderer;
import com.github.wolfshotz.wyrmroost.entities.dragon.*;
import com.github.wolfshotz.wyrmroost.items.LazySpawnEggItem;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fmllegacy.network.FMLPlayMessages;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND;

@SuppressWarnings("unchecked")
public class WREntities<E extends Entity> extends EntityType<E>
{
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITIES, Wyrmroost.MOD_ID);

    /*public static final RegistryObject<EntityType<LesserDesertwyrmEntity>> LESSER_DESERTWYRM = creature("lesser_desertwyrm", LesserDesertwyrmEntity::new)
            .size(0.6f, 0.2f)
            .attributes(LesserDesertwyrmEntity::getAttributeSupplier)
            .spawnPlacement(ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LesserDesertwyrmEntity::getSpawnPlacement)
            .spawnEgg(0xD6BCBC, 0xDEB6C7)
            .renderModel(() -> LesserDesertwyrmModel::new)
            .packetInterval(5)
            .build();

    public static final RegistryObject<EntityType<OverworldDrakeEntity>> OVERWORLD_DRAKE = creature("overworld_drake", OverworldDrakeEntity::new)
            .size(2.376f, 2.58f)
            .attributes(OverworldDrakeEntity::getAttributeSupplier)
            .spawnPlacement()
            .spawnEgg(0x788716, 0x3E623E)
            .dragonEgg(new DragonEggProperties(0.35f, 0.6f, 18000))
            .renderModel(() -> OverworldDrakeModel::new)
            .trackingRange(10)
            .build();

    public static final RegistryObject<EntityType<SilverGliderEntity>> SILVER_GLIDER = creature("silver_glider", SilverGliderEntity::new)
            .size(1.5f, 0.75f)
            .attributes(SilverGliderEntity::getAttributeSupplier)
            .spawnPlacement(SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SilverGliderEntity::getSpawnPlacement)
            .spawnEgg(0xC8C8C8, 0xC4C4C4)
            .dragonEgg(new DragonEggProperties(0.2f, 0.35f, 12000))
            .renderModel(() -> SilverGliderModel::new)
            .trackingRange(8)
            .build();*/

    public static final RegistryObject<EntityType<RoostStalkerEntity>> ROOSTSTALKER = creature("roost_stalker", RoostStalkerEntity::new)
            .size(0.65f, 0.5f)
            .spawnPlacement()
            .spawnEgg(0x52100D, 0x959595)
            .attributes(() -> RoostStalkerEntity.getAttributeSupplier())
            //.dragonEgg(new DragonEggProperties(0.175f, 0.3f, 6000))
            .build();

    /*public static final RegistryObject<EntityType<ButterflyLeviathanEntity>> BUTTERFLY_LEVIATHAN = ofGroup("butterfly_leviathan", ButterflyLeviathanEntity::new, MobCategory.WATER_CREATURE)
            .size(4f, 3f)
            .attributes(ButterflyLeviathanEntity::getAttributeSupplier)
            .spawnPlacement(SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.OCEAN_FLOOR_WG, ButterflyLeviathanEntity::getSpawnPlacement)
            .spawnEgg(0x17283C, 0x7A6F5A)
            .dragonEgg(new DragonEggProperties(0.5f, 0.8f, 40000).setConditions(Entity::isInWater))
            .renderModel(() -> ButterflyLeviathanModel::new)
            .trackingRange(8)
            .build();

    public static final RegistryObject<EntityType<DragonFruitDrakeEntity>> DRAGON_FRUIT_DRAKE = creature("dragon_fruit_drake", DragonFruitDrakeEntity::new)
            .size(1.5f, 1.9f)
            .attributes(DragonFruitDrakeEntity::getAttributeSupplier)
            .spawnPlacement(SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DragonFruitDrakeEntity::getSpawnPlacement)
            .spawnEgg(0xe05c9a, 0x788716)
            .dragonEgg(new DragonEggProperties(0.25f, 0.35f, 9600))
            .renderModel(() -> DragonFruitDrakeModel::new)
            .build();

    public static final RegistryObject<EntityType<CanariWyvernEntity>> CANARI_WYVERN = creature("canari_wyvern", CanariWyvernEntity::new)
            .size(0.65f, 0.85f)
            .attributes(CanariWyvernEntity::getAttributeSupplier)
            .spawnPlacement(SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, TameableDragonEntity::canFlyerSpawn)
            .spawnEgg(0x1D1F28, 0x492E0E)
            .dragonEgg(new DragonEggProperties(0.175f, 0.275f, 6000).setConditions(c -> c.level.getBlockState(c.blockPosition().below()).getBlock() == Blocks.JUNGLE_LEAVES))
            .renderModel(() -> CanariWyvernModel::new)
            .build();

    public static final RegistryObject<EntityType<RoyalRedEntity>> ROYAL_RED = creature("royal_red", RoyalRedEntity::new)
            .size(3f, 3.9f)
            .attributes(RoyalRedEntity::getAttributeSupplier)
            .spawnPlacement(SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, TameableDragonEntity::canFlyerSpawn)
            .spawnEgg(0x8a0900, 0x0)
            .dragonEgg(new DragonEggProperties(0.45f, 0.7f, 72000))
            .renderModel(() -> RoyalRedModel::new)
            .fireImmune()
            .trackingRange(11)
            .build();

    public static final RegistryObject<EntityType<CoinDragonEntity>> COIN_DRAGON = creature("coin_dragon", CoinDragonEntity::new)
            .size(0.35f, 0.435f)
            .renderModel(() -> CoinDragonModel::new)
            .attributes(CoinDragonEntity::getAttributeSupplier)
            .trackingRange(2)
            .build();

    public static final RegistryObject<EntityType<AlpineEntity>> ALPINE = creature("alpine", AlpineEntity::new)
            .size(2f, 2f)
            .attributes(AlpineEntity::getAttributeSupplier)
            .spawnPlacement(SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, TameableDragonEntity::canFlyerSpawn)
            .spawnEgg(0xe3f8ff, 0xa8e9ff)
            .dragonEgg(new DragonEggProperties(0.35f, 0.55f, 12000))
            .renderModel(() -> AlpineModel::new)
            .trackingRange(9)
            .build();

    public static final RegistryObject<EntityType<DragonEggEntity>> DRAGON_EGG = WREntities.<DragonEggEntity>ofGroup("dragon_egg", DragonEggEntity::new, MobCategory.MISC)
            .renderer(() -> DragonEggRenderer::new)
            .noSummon()
            .clientFactory(DragonEggEntity::new)
            .trackingRange(4)
            .packetInterval(5)
            .build();

    public static final RegistryObject<EntityType<GeodeTippedArrowEntity>> GEODE_TIPPED_ARROW = WREntities.<GeodeTippedArrowEntity>ofGroup("geode_tipped_arrow", GeodeTippedArrowEntity::new, MobCategory.MISC)
            .size(0.5f, 0.5f)
            .renderer(() -> GeodeTippedArrowRenderer::new)
            .clientFactory(GeodeTippedArrowEntity::new)
            .trackingRange(4)
            .packetInterval(10)
            .build();

    public static final RegistryObject<EntityType<FireBreathEntity>> FIRE_BREATH = WREntities.<FireBreathEntity>ofGroup("fire_breath", FireBreathEntity::new, MobCategory.MISC)
            .size(0.75f, 0.75f)
            .renderer(() -> BreathWeaponRenderer::new)
            .noSave()
            .noSummon()
            .packetInterval(10)
            .build();

    public static final RegistryObject<EntityType<WindGustEntity>> WIND_GUST = WREntities.<WindGustEntity>ofGroup("wind_gust", WindGustEntity::new, MobCategory.MISC)
            .size(4, 4)
            .renderer(() -> EmptyRenderer::new)
            .noSave()
            .noSummon()
            .packetInterval(10)
            .build();

    public static final RegistryObject<EntityType<SoulCrystalEntity>> SOUL_CRYSTAL = WREntities.<SoulCrystalEntity>ofGroup("soul_crystal", SoulCrystalEntity::new, MobCategory.MISC)
            .size(0.25f, 0.25f)
            .renderer(() -> ClientEvents::spriteRenderer)
            .trackingRange(4)
            .packetInterval(10)
            .build(); */

    @Nullable public final Supplier<AttributeSupplier> attributes;
    @Nullable public final SpawnPlacementEntry<E> spawnPlacement;
    //@Nullable public final DragonEggProperties eggProperties;

    public WREntities(EntityType.EntityFactory<E> factory, MobCategory group, boolean serialize, boolean summon, boolean fireImmune, boolean spawnsFarFromPlayer, ImmutableSet<Block> immuneTo, EntityDimensions size, int trackingRange, int tickRate, Predicate<EntityType<?>> velocityUpdateSupplier, ToIntFunction<EntityType<?>> trackingRangeSupplier, ToIntFunction<EntityType<?>> updateIntervalSupplier, BiFunction<FMLPlayMessages.SpawnEntity, Level, E> customClientFactory, Supplier<AttributeSupplier> attributes, SpawnPlacementEntry<E> spawnPlacement/*, DragonEggProperties props*/)
    {
        super(factory, group, serialize, summon, fireImmune, spawnsFarFromPlayer, immuneTo, size, trackingRange, tickRate, velocityUpdateSupplier, trackingRangeSupplier, updateIntervalSupplier, customClientFactory);
        this.attributes = attributes;
        this.spawnPlacement = spawnPlacement;
        //this.eggProperties = props;
    }


    private static <T extends Entity> Builder<T> creature(String name, EntityType.EntityFactory<T> factory)
    {
        return new Builder<>(name, factory, MobCategory.CREATURE);
    }

    private static <T extends Entity> Builder<T> ofGroup(String name, EntityType.EntityFactory<T> factory, MobCategory group)
    {
        return new Builder<>(name, factory, group);
    }

    public static class Attributes
    {
        public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Wyrmroost.MOD_ID);

        public static final RegistryObject<Attribute> PROJECTILE_DAMAGE = ranged("generic.projectileDamage", 2d, 0, 2048d);

        private static RegistryObject<Attribute> ranged(String name, double defaultValue, double min, double max)
        {
            return register(name.toLowerCase().replace('.', '_'), () -> new RangedAttribute("attribute.name." + name, defaultValue, min, max));
        }

        private static RegistryObject<Attribute> register(String name, Supplier<Attribute> attribute)
        {
            return REGISTRY.register(name, attribute);
        }
    }

    public static class Tags
    {
        public static final Tag<EntityType<?>> SOUL_BEARERS = bind("soul_bearers");

        private static Tag<EntityType<?>> bind(String name)
        {
            return EntityTypeTags.bind(Wyrmroost.MOD_ID + ":" + name);
        }
    }

    private static class Builder<T extends Entity>
    {
        private final String name;
        private final EntityType.EntityFactory<T> factory;
        private final MobCategory category;
        private ImmutableSet<Block> immuneTo = ImmutableSet.of();
        private boolean serialize = true;
        private boolean summon = true;
        private boolean fireImmune;
        private boolean canSpawnFarFromPlayer;
        private int trackingRange = 5;
        private int packetInterval = 3;
        private boolean updatesVelocity = true;
        private EntityDimensions size = EntityDimensions.scalable(0.6f, 1.8f);
        //private DragonEggProperties dragonEggProperties;
        private Supplier<AttributeSupplier> attributes = null;
        private SpawnPlacementEntry<T> spawnPlacement;
        private BiFunction<FMLPlayMessages.SpawnEntity, Level, T> customClientFactory;

        private RegistryObject<EntityType<T>> registered;

        private Builder(String name, EntityType.EntityFactory<T> factory, MobCategory group)
        {
            this.name = name;
            this.factory = factory;
            this.category = group;
            this.canSpawnFarFromPlayer = group == MobCategory.CREATURE || group == MobCategory.MISC;
        }

        private Builder<T> size(float width, float height)
        {
            this.size = EntityDimensions.scalable(width, height);
            return this;
        }

        private Builder<T> noSummon()
        {
            this.summon = false;
            return this;
        }

        private Builder<T> noSave()
        {
            this.serialize = false;
            return this;
        }

        private Builder<T> fireImmune()
        {
            this.fireImmune = true;
            return this;
        }

        private Builder<T> immuneTo(Block... blocks)
        {
            this.immuneTo = ImmutableSet.copyOf(blocks);
            return this;
        }

        private Builder<T> canSpawnFarFromPlayer()
        {
            this.canSpawnFarFromPlayer = true;
            return this;
        }

        private Builder<T> trackingRange(int trackingRange)
        {
            this.trackingRange = trackingRange;
            return this;
        }

        private Builder<T> packetInterval(int rate)
        {
            this.packetInterval = rate;
            return this;
        }

        private Builder<T> noVelocityUpdates()
        {
            this.updatesVelocity = false;
            return this;
        }

        private Builder<T> clientFactory(BiFunction<FMLPlayMessages.SpawnEntity, Level, T> clientFactory)
        {
            this.customClientFactory = clientFactory;
            return this;
        }

        private Builder<T> spawnEgg(int primColor, int secColor)
        {
            WRItems.register(name + "_spawn_egg", () -> new LazySpawnEggItem<>(registered, primColor, secColor));
            return this;
        }



        // needs to be supplier to accomadate for attributes deferred registered
        private Builder<T> attributes(Supplier<AttributeSupplier.Builder> map)
        {
            this.attributes = () -> map.get().build();
            return this;
        }

        private <F extends Mob> Builder<T> spawnPlacement(SpawnPlacements.Type type, SpawnPlacements.SpawnPredicate<F> predicate)
        {
            this.spawnPlacement = new SpawnPlacementEntry<T>(type, ((SpawnPlacements.SpawnPredicate<T>) predicate));
            return this;
        }

        private Builder<T> spawnPlacement()
        {
            return spawnPlacement(ON_GROUND, Animal::checkMobSpawnRules);
        }

        /*private Builder<T> dragonEgg(DragonEggProperties props)
        {
            this.dragonEggProperties = props;
            return this;
        }*/

        private RegistryObject<EntityType<T>> build()
        {
            return registered = REGISTRY.register(name, () -> new WREntities<>(factory, category, serialize, summon, fireImmune, canSpawnFarFromPlayer, immuneTo, size, trackingRange, packetInterval, t -> updatesVelocity, t -> trackingRange, t -> packetInterval, customClientFactory, attributes, spawnPlacement/*, dragonEggProperties*/));
        }
    }

    private static class SpawnPlacementEntry<E extends Entity>
    {
        final SpawnPlacements.Type placement;
        final SpawnPlacements.SpawnPredicate<E> predicate;

        SpawnPlacementEntry(SpawnPlacements.Type placement, SpawnPlacements.SpawnPredicate<E> predicate) {
            this.placement = placement;
            this.predicate = predicate;
        }
    }
}
