package net.violetunderscore.every5minutes;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.TeleportTarget;
import net.violetunderscore.every5minutes.returns.returnRandoms;
import net.violetunderscore.every5minutes.vars.TickData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Every5Minutes implements ModInitializer {
	public static final String MOD_ID = "violet_every5minutes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Identifier HEALTH_REDUCTION_ID = Identifier.of("e5m", "health_reduction");

	@Override
	public void onInitialize() {
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			ServerWorld world = server.getOverworld();
			TickData data = world
					.getPersistentStateManager()
					.getOrCreate(TickData.TYPE, "e5m_tick_data");

			if (data.active) {
				if (++data.ticks >= data.interval * (server.getTickManager().getTickRate() / 20)) {
					data.ticks = 0;
					data.counter++;
					switch(data.challenge) {
						case 1:
							teleportEverything(server);
							break;
						case 2:
							increaseTickRate(server, data);
							break;
						case 3:
							randomPotionEffect(server, data);
							break;
						case 4:
							randomSwarm(server, data);
							break;
                        case 5:
                            loseHealth(server, data);
                            break;
                        case 6:
                            mitosis(server, data);
                            break;
						default:
							StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
							server.sendMessage(Text.literal("err no. 1: no valid challenge is selected"));
							server.sendMessage(Text.literal(getDebugLocation()));
							break;
					}
				}

				if (data.challenge == 5) {
					for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
						var attrInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
						if (attrInstance == null) continue;
						if (attrInstance.getModifier(HEALTH_REDUCTION_ID) == null) {
							var modifier = new EntityAttributeModifier(HEALTH_REDUCTION_ID, -data.counter, Operation.ADD_VALUE);
							attrInstance.addPersistentModifier(modifier);
							if (data.counter >= 20) {
								player.setHealth(0);
							} else {
								if (player.getHealth() > player.getMaxHealth()) {
									player.setHealth(player.getMaxHealth());
								}
							}
						}
					}
				}
			}

			data.markDirty();
		});
	}





	/*Challenges*/
	public static void teleportEverything(MinecraftServer server) {
		//server.getCommandManager().executeWithPrefix(server.getCommandSource().withLevel(4), "tp @e @r");
        List<Entity> entities = new ArrayList<>();
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        Random r = new Random();
        ServerPlayerEntity p = players.get(r.nextInt(players.size()));
        for (ServerWorld world : server.getWorlds()) {
            for (Entity entity : world.iterateEntities()) {
                entities.add(entity);
            }
        }
        for (Entity entity : entities) {
            entity.teleport(p.getServerWorld(), p.getX(), p.getY(), p.getZ(),
                    EnumSet.noneOf(PositionFlag.class), p.getYaw(), p.getPitch());
        }
	}
	public void increaseTickRate(MinecraftServer server, TickData data) {
		server.getTickManager().setTickRate(20 + (data.counter + 2));
	}
	public void randomPotionEffect(MinecraftServer server, TickData data) {

		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			Identifier id = Identifier.tryParse(
					returnRandoms.returnPotionEffect()
			);
			RegistryEntry<StatusEffect> effectEntry = Registries.STATUS_EFFECT.getEntry(Registries.STATUS_EFFECT.get(id));

			Random random = new Random();
			int amp = random.nextInt(5) + 1;

			if (effectEntry == StatusEffects.INSTANT_DAMAGE || effectEntry == StatusEffects.INSTANT_HEALTH) {
				player.addStatusEffect(new StatusEffectInstance(
						effectEntry,
						1,
						amp
				));
			} else {
				player.addStatusEffect(new StatusEffectInstance(
						effectEntry,
						data.interval,
						amp
				));
			}
		}
	}
	public void randomSwarm(MinecraftServer server, TickData data) {
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			Identifier id = Identifier.tryParse(
					//returnRandoms.returnMob()
					"minecraft:warden"
			);
			for (int i = 0; i < data.counter; i++) {
				spawnMob(Objects.requireNonNull(player.getServer()).getWorld(player.getWorld().getRegistryKey()), player.getBlockPos(), id);
			}
		}
	}
	public void loseHealth(MinecraftServer server, TickData data) {
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

			var attrInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
			if (attrInstance == null) continue;

			if (attrInstance.getModifier(HEALTH_REDUCTION_ID) != null) {
				attrInstance.removeModifier(HEALTH_REDUCTION_ID);
			}

			var modifier = new EntityAttributeModifier(HEALTH_REDUCTION_ID, -data.counter, Operation.ADD_VALUE);
			attrInstance.addPersistentModifier(modifier);

			if (data.counter >= 20) {
				player.setHealth(0);
			} else {
				if (player.getHealth() > player.getMaxHealth()) {
					player.setHealth(player.getMaxHealth());
				}
			}
		}
	}
    public static void mitosis(MinecraftServer server, TickData data) {
        for (ServerWorld world : server.getWorlds()) {
            List<Entity> entities = new ArrayList<>();
            for (Entity entity : world.iterateEntities()) {
                entities.add(entity);
            }
            for (Entity entity : entities) {
                if (!(entity instanceof PlayerEntity) && entity instanceof LivingEntity) {
                    cloneMob(entity);
                }
            }
        }
    }





	/*Extra code for randomSwarm*/
	public static void spawnMob(ServerWorld world, BlockPos pos, Identifier id) {
		EntityType<?> entityType = Registries.ENTITY_TYPE.get(id);

		if (entityType != null) {
			Entity entity = entityType.create(world);
			if (entity != null) {
				BlockPos pos2 = SpawnLocation(world, pos, entity);
				entity.refreshPositionAndAngles(pos2, 0, 0);
				MobEntity mob = (MobEntity) entity;
				/*put items in hands of mobs*/
				{
					if (entity instanceof AbstractSkeletonEntity) {
						if (entity instanceof WitherSkeletonEntity) {
							mob.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
						} else {
							mob.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
						}
					} else if (entity instanceof PiglinBruteEntity) {
						mob.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
					} else if (entity instanceof PiglinEntity) {
						if (new Random().nextInt(1, 2) == 1) {
							mob.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
						} else {
							mob.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
						}
					} else if (entity instanceof PillagerEntity) {
						mob.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
					} else if (entity instanceof VindicatorEntity) {
						mob.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
					} else if (entity instanceof VexEntity) {
						mob.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
					}
				}
				/*change data of mobs*/
				{
					if (entity instanceof SlimeEntity) {
						SlimeEntity slimeMob = (SlimeEntity) mob;
						int r = new Random().nextInt(1, 4);
						if (r == 3) {r++;}
						slimeMob.setSize(r, true);
						world.spawnEntity(slimeMob);
					}
					else if (entity instanceof WardenEntity) {
						WardenEntity wardenMob = (WardenEntity) mob;
						wardenMob.getBrain().remember(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, 1200);
						wardenMob.getBrain().remember(MemoryModuleType.IS_EMERGING, Unit.INSTANCE, 134);
						world.spawnEntity(wardenMob);
					}
					else {
						world.spawnEntity(mob);
					}
				}
			} else {
				System.err.println("Failed to create an entity for type: " + id.toString());
			}
		} else {
			System.err.println("EntityType not found for key: " + id.toString());
		}

	}
	private static BlockPos SpawnLocation(ServerWorld world, BlockPos pos, Entity entity) {
		if (entity instanceof ZombieEntity // 2x1
				|| (entity instanceof AbstractSkeletonEntity && !(entity instanceof WitherSkeletonEntity))
				|| entity instanceof CreeperEntity
				|| entity instanceof BreezeEntity
				|| entity instanceof CowEntity
				|| entity instanceof IllagerEntity
				|| entity instanceof GoatEntity
				|| entity instanceof LlamaEntity
				|| entity instanceof AbstractPiglinEntity
				|| entity instanceof SnowGolemEntity
				|| entity instanceof VillagerEntity
				|| entity instanceof WitchEntity
		) {
			return CalculateSpawnLocation(world, pos, 12, 1, 2, false, true);
		} else if (entity instanceof BeeEntity // 1x1 hydrophobic
				|| entity instanceof EndermiteEntity
		) {
			return CalculateSpawnLocation(world, pos, 12, 1, 1, true, true);
		} else if (entity instanceof CamelEntity // 3x3
				|| entity instanceof IronGolemEntity
				|| entity instanceof RavagerEntity
		) {
			return CalculateSpawnLocation(world, pos, 12, 2, 3, false, true);
		} else if (entity instanceof AbstractHorseEntity // 3x2
				|| entity instanceof ElderGuardianEntity
				|| entity instanceof HoglinEntity
				|| entity instanceof ZoglinEntity
				|| entity instanceof PandaEntity
				|| entity instanceof PolarBearEntity
				|| entity instanceof SnifferEntity
		) {
			return CalculateSpawnLocation(world, pos, 12, 2, 2, false, true);
		} else if (entity instanceof TurtleEntity // 3x1
				|| (entity instanceof SpiderEntity && !(entity instanceof CaveSpiderEntity))
		) {
			return CalculateSpawnLocation(world, pos, 12, 2, 1, false, true);
		} else if (entity instanceof EndermanEntity) { //1x3 hydrophobic
			return CalculateSpawnLocation(world, pos, 12, 1, 3, true, true);
		} else if (entity instanceof MagmaCubeEntity) { // 1x1 lavaproof
			return CalculateSpawnLocation(world, pos, 12, 1, 1, false, false);
		} else if (entity instanceof StriderEntity) { // 1x2 lavaproof
			return CalculateSpawnLocation(world, pos, 12, 1, 2, false, false);
		} else if (entity instanceof BlazeEntity) { // 1x2 hydrophobic lavaproof
			return CalculateSpawnLocation(world, pos, 12, 1, 2, true, false);
		} else if (entity instanceof GhastEntity) { // 5x5 lavaproof
			return CalculateSpawnLocation(world, pos, 12, 3, 5, false, false);
		} else if (entity instanceof WitherSkeletonEntity // 3x1 lavaproof
				|| entity instanceof WardenEntity
		) {
			return CalculateSpawnLocation(world, pos, 12, 1, 3, false, false);
		} // default is 1x1
		return CalculateSpawnLocation(world, pos, 12, 1, 1, false, true);
	}
	private static BlockPos CalculateSpawnLocation(ServerWorld world, BlockPos pos, int r, int w, int h, boolean hydrophobic, boolean pyrophobic) {
		int nx = pos.getX(); //yeah,,, im not bothering to make ts readable my mind is already fried lmao
		int nz = pos.getZ();
		int ny = pos.getY();
		boolean success = false;
		for (int i = 0; i < 16; i++) {
			Random random = new Random();
			int x = pos.getX() + random.nextInt(r * 2 + 1) - r;
			int z = pos.getZ() + random.nextInt(r * 2 + 1) - r;
			int y = pos.getY() - 1;

			for (int vv = 0; vv < r + 1; vv = plusMinusFluctuate(vv)) {
				boolean fail = false;
				int v_y = 0;
				if (isValidBlock(world, BlockPos.ofFloored(x, vv + y, z), false, hydrophobic, pyrophobic)) {
					for (v_y = 1; v_y < h + 1; v_y++) {
						for (int v_x = -(w-1); v_x < w; v_x++) {
							for (int v_z = -(w-1); v_z < w; v_z++) {
								if (!isValidBlock(world, BlockPos.ofFloored(v_x + x, v_y + vv + y, v_z + z), true, hydrophobic, pyrophobic)) {
									fail = true;
								}
								if (fail) {break;}
							}
							if (fail) {break;}
						}
						if (fail) {break;}
					}
				} else {fail = true;}
				if (!fail) {
					success = true;
					nx = x;
					ny = vv + y;
					nz = z;
					break;
				}
			}
			if (success) {
				break;
			}
		}
		return BlockPos.ofFloored(nx, ny + 1, nz);
	}
	public static boolean isValidBlock (ServerWorld world, BlockPos pos, boolean air, boolean hydrophobic, boolean pyrophobic) {
		BlockState state = world.getBlockState(pos);
		VoxelShape shape = state.getCollisionShape(world, pos);
		if (!air) {
			if (!shape.isEmpty() && (!pyrophobic || state.getBlock() != Blocks.MAGMA_BLOCK)) {
				return true;
			}
			return false;
		}
		if (shape.isEmpty() && (!pyrophobic || state.getBlock() != Blocks.LAVA) && (!hydrophobic || state.getBlock() != Blocks.WATER)) {
			return true;
		}
		return false;
	}
	private static int plusMinusFluctuate(int v) {
		if (v == 0) {
			return 1;
		}
		if (v < 0) {
			return -v + 1;
		}
		return -v;
	}

    /*Extra code for mitosis*/
    public static void cloneMob(Entity e) {
        if (e == null) return;
        NbtCompound nbt = new NbtCompound();
        e.writeNbt(nbt);
        nbt.remove("UUID");

        Identifier id = EntityType.getId(e.getType());
        if (id == null) return;
        nbt.putString("id", id.toString());

        Entity e2 = EntityType.loadEntityWithPassengers(nbt, e.getWorld(), entity -> {
            entity.refreshPositionAndAngles(
                    e.getX(),
                    e.getY(),
                    e.getZ(),
                    e.getYaw(),
                    e.getPitch()
            );
            return entity;
        });
        if (e2 != null) {
            e.getWorld().spawnEntity(e2);
        }
    }





	/*debug*/
	public static String getDebugLocation() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		if (stackTrace.length > 2) {
			StackTraceElement caller = stackTrace[2];
			return "at " + caller.getClassName() + "." + caller.getMethodName() +
					" (" + caller.getFileName() + ":" + caller.getLineNumber() + ")";
		} else {
			return "Unknown location";
		}
	}
}