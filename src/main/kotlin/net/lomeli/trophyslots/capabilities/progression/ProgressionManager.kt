package net.lomeli.trophyslots.capabilities.progression

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.capabilities.slots.SlotManager
import net.lomeli.trophyslots.core.network.MessageUpdateClientProgress
import net.lomeli.trophyslots.core.triggers.AllTriggers
import net.minecraft.entity.Entity
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.*
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.translation.I18n
import net.minecraftforge.common.util.FakePlayer
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent
import net.minecraftforge.oredict.OreDictionary
import java.util.regex.Pattern

object ProgressionManager {
    @JvmStatic val PLAYER_PROGRESSION = ResourceLocation(TrophySlots.MOD_ID, "progression_trophy_slots")
    private val registerProgressions = HashMap<String, Progression>()

    init {
        // THE BASICS
        registerProgression("open_inventory", "progress.trophyslots.open_inventory", ItemStack(Blocks.GRASS))
        registerProgression("obtain_wood", "progress.trophyslots.obtain_wood", ItemStack(Blocks.LOG))
        registerProgression("craft_crafting_table", "progress.trophyslots.craft_crafting_table",
                ItemStack(Blocks.CRAFTING_TABLE), "obtain_wood")
        registerProgression("craft_axe", "progress.trophyslots.craft_axe", ItemStack(Items.WOODEN_AXE), "craft_crafting_table")
        registerProgression("craft_sword", "progress.trophyslots.craft_sword", ItemStack(Items.IRON_SWORD), "craft_crafting_table")
        registerProgression("craft_pick_axe", "progress.trophyslots.craft_pick_axe", ItemStack(Items.STONE_PICKAXE), "craft_crafting_table")
        registerProgression("craft_torch", "progress.trophyslots.craft_torch", ItemStack(Blocks.TORCH), "obtain_wood")
        registerProgression("place_chest", "progress.trophyslots.place_chest", ItemStack(Blocks.CHEST), "craft_crafting_table")
        registerProgression("craft_bow", "progress.trophyslots.craft_bow", ItemStack(Items.BOW), "craft_crafting_table")
        // FOOD
        registerProgression("obtain_wheat", "progress.trophyslots.obtain_wheat", ItemStack(Items.WHEAT))
        registerProgression("obtain_potato", "progress.trophyslots.obtain_potato", ItemStack(Items.POTATO))
        registerProgression("obtain_carrot", "progress.trophyslots.obtain_carrot", ItemStack(Items.CARROT))
        registerProgression("obtain_pork_chop", "progress.trophyslots.obtain_pork_chop", ItemStack(Items.PORKCHOP))
        registerProgression("obtain_steak", "progress.trophyslots.obtain_steak", ItemStack(Items.BEEF))
        registerProgression("obtain_chicken", "progress.trophyslots.obtain_chicken", ItemStack(Items.CHICKEN))
        registerProgression("obtain_rabbit", "progress.trophyslots.obtain_rabbit", ItemStack(Items.RABBIT))
        registerProgression("craft_bread", "progress.trophyslots.craft_bread", ItemStack(Items.BREAD), "obtain_wheat")
        registerProgression("obtain_fish", "progress.trophyslots.obtain_fish", ItemStack(Items.FISH))
        registerProgression("craft_cake", "progress.trophyslots.craft_cake", ItemStack(Items.CAKE), "obtain_wheat")
        registerProgression("craft_pie", "progress.trophyslots.craft_pie", ItemStack(Items.PUMPKIN_PIE))
        // MINING
        registerProgression("obtain_iron_ore", "progress.trophyslots.obtain_iron_ore", ItemStack(Blocks.IRON_ORE), "craft_pick_axe")
        registerProgression("craft_redstone_torch", "progress.trophyslots.craft_redstone_torch", ItemStack(Items.REDSTONE), "obtain_iron_ore")
        registerProgression("craft_anvil", "progress.trophyslots.craft_anvil", ItemStack(Blocks.ANVIL), "obtain_iron_ore")
        registerProgression("craft_gold_helmet", "progress.trophyslots.craft_gold_helmet", ItemStack(Items.GOLDEN_HELMET), "obtain_iron_ore")
        registerProgression("obtain_diamonds", "progress.trophyslots.obtain_diamonds", ItemStack(Items.DIAMOND), "obtain_iron_ore")
        registerProgression("obtain_emerald", "progress.trophyslots.obtain_emerald", ItemStack(Items.EMERALD), "obtain_iron_ore")
        registerProgression("craft_blue_block", "progress.trophyslots.craft_blue_block", ItemStack(Blocks.LAPIS_BLOCK))
        // NETHER
        registerProgression("obtain_obsidian", "progress.trophyslots.obtain_obsidian", ItemStack(Blocks.OBSIDIAN), "obtain_diamonds")
        registerProgression("enter_nether", "progress.trophyslots.enter_nether", ItemStack(Items.FIRE_CHARGE), "obtain_obsidian")
        registerProgression("obtain_blaze_rod", "progress.trophyslots.obtain_blaze_rod", ItemStack(Items.BLAZE_ROD), "enter_nether")
        registerProgression("obtain_nether_skull", "progress.trophyslots.obtain_nether_skull", ItemStack(Items.SKULL, 1, 1), "enter_nether")
        registerProgression("obtain_soul_sand", "progress.trophyslots.obtain_soul_sand", ItemStack(Blocks.SOUL_SAND), "enter_nether")
        registerProgression("obtain_nether_star", "progress.trophyslots.obtain_nether_star", ItemStack(Items.NETHER_STAR), "obtain_nether_skull")
        // END
        registerProgression("kill_enderman", "progress.trophyslots.kill_enderman", ItemStack(Items.ENDER_PEARL), "craft_sword")
        registerProgression("craft_ender_eye", "progress.trophyslots.craft_ender_eye", ItemStack(Items.ENDER_EYE), "obtain_blaze_rod")
        registerProgression("enter_end", "progress.trophyslots.enter_end", ItemStack(Blocks.END_PORTAL_FRAME), "craft_ender_eye")
        registerProgression("kill_ender_dragon", "progress.trophyslots.kill_ender_dragon", ItemStack(Blocks.DRAGON_EGG), "enter_end")
        registerProgression("obtain_elytra", "progress.trophyslots.obtain_elytra", ItemStack(Items.ELYTRA), "kill_ender_dragon")
        // FUN
        registerProgression("one_heart", "progress.trophyslots.one_heart", ItemStack(Items.POTIONITEM, 1, 8197))
        registerProgression("kill_player", "progress.trophyslots.kill_player", ItemStack(Items.SKULL))
        registerProgression("obtain_totem_death", "progress.trophyslots.obtain_totem_death", ItemStack(Items.TOTEM_OF_UNDYING))
    }

    fun registerProgression(progression: Progression) {
        if (!progressionRegistered(progression.progressionID)) registerProgressions.put(progression.progressionID, progression)
    }

    fun registerProgression(id: String, name: String, icon: ItemStack, parentID: String?) {
        registerProgression(Progression(id, name, icon, parentID))
    }

    fun registerProgression(id: String, name: String, icon: ItemStack) {
        registerProgression(id, name, icon, null)
    }

    fun getProgression(id: String): Progression? = registerProgressions[id]

    fun progressionRegistered(id: String): Boolean = registerProgressions.keys.contains(id)

    fun getPlayerProgressionData(player: EntityPlayer): IPlayerProgression? {
        val cap = player.getCapability(ProgressionProvider.PLAYER_PROGRESSION!!, null)
        return cap
    }

    fun progressionMessage(player: EntityPlayer, id: String) {
        val playerMP = FMLCommonHandler.instance().minecraftServerInstance.playerList.getPlayerByUUID(player.uniqueID)
        if (TrophySlots.useProgressionUnlocks) {
            val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
            slotInfo.unlockSlot(1)
            var msg = "msg.trophyslots.unlock"
            if (slotInfo.isAtMaxSlots()) msg = "msg.trophyslots.unlock_all"
            playerMP.sendStatusMessage(TextComponentTranslation(msg), true)
            AllTriggers.UNLOCK_SLOT.trigger(playerMP)
            SlotManager.updateClient(playerMP, slotInfo)
        }
        val progression = getProgression(id)!!
        TrophySlots.packetHandler!!.sendTo(MessageUpdateClientProgress(getPlayerProgressionData(player)!!.getUnlockList()), playerMP)
        var text = I18n.translateToLocal("progress.trophyslots.unlock_message")
                .replace("%user%", player.displayNameString)
                .replace("%progress%", I18n.translateToLocal(progression.progressionName))
                .replace("%progress_description%", I18n.translateToLocal(progression.getDescription()))
        FMLCommonHandler.instance().minecraftServerInstance.playerList.sendMessage(ITextComponent.Serializer.fromJsonLenient(text))
    }

    @SubscribeEvent fun attachProgressionCapability(event: AttachCapabilitiesEvent<Entity>) {
        if (event.`object` !is EntityPlayer) return
        event.addCapability(PLAYER_PROGRESSION, ProgressionProvider())
    }

    @SubscribeEvent fun playerCraftItem(event: PlayerEvent.ItemCraftedEvent) {
        if (!TrophySlots.useProgressionUnlocks) return
        val stack = event.crafting
        val player = event.player
        if (stack.isEmpty) return
        val progression = getPlayerProgressionData(player)!!
        if (stack.item == Item.getItemFromBlock(Blocks.CRAFTING_TABLE) &&
                progression.givePlayerProgression("craft_crafting_table"))
            progressionMessage(player, "craft_crafting_table")
        if (stack.item is ItemAxe && progression.givePlayerProgression("craft_axe"))
            progressionMessage(player, "craft_axe")
        if (stack.item is ItemSword && progression.givePlayerProgression("craft_sword"))
            progressionMessage(player, "craft_sword")
        if (stack.item is ItemPickaxe && progression.givePlayerProgression("craft_pick_axe"))
            progressionMessage(player, "craft_pick_axe")
        if (isItemRegisteredAsOre(stack, "torch") && progression.givePlayerProgression("craft_torch"))
            progressionMessage(player, "craft_torch")
        if (stack.item == Items.BREAD && progression.givePlayerProgression("craft_bread"))
            progressionMessage(player, "craft_bread")
        if (stack.item == Items.CAKE && progression.givePlayerProgression("craft_cake"))
            progressionMessage(player, "craft_cake")
        if (stack.item == Items.PUMPKIN_PIE && progression.givePlayerProgression("craft_pie"))
            progressionMessage(player, "craft_pie")
        if (stack.item is ItemBow && progression.givePlayerProgression("craft_bow"))
            progressionMessage(player, "craft_bow")
        if (stack.item == Item.getItemFromBlock(Blocks.REDSTONE_TORCH) &&
                progression.givePlayerProgression("craft_redstone_torch"))
            progressionMessage(player, "craft_redstone_torch")
        if (stack.item == Item.getItemFromBlock(Blocks.ANVIL) && progression.givePlayerProgression("craft_anvil"))
            progressionMessage(player, "craft_anvil")
        if (stack.item == Items.GOLDEN_HELMET && progression.givePlayerProgression("craft_gold_helmet"))
            progressionMessage(player, "craft_gold_helmet")
        if ((stack.item == Item.getItemFromBlock(Blocks.LAPIS_BLOCK) ||
                (stack.item == Item.getItemFromBlock(Blocks.WOOL) && stack.metadata == 11) ||
                stack.item == Item.getItemFromBlock(Blocks.DIAMOND_BLOCK)) &&
                progression.givePlayerProgression("craft_blue_block"))
            progressionMessage(player, "craft_blue_block")
        if (stack.item == Items.ENDER_EYE && progression.givePlayerProgression("craft_ender_eye"))
            progressionMessage(player, "craft_ender_eye")
    }

    @SubscribeEvent(priority = EventPriority.LOWEST) fun playerPickUpItem(event: EntityItemPickupEvent) {
        if (event.isCanceled || !TrophySlots.useProgressionUnlocks) return
        val player = event.entityPlayer
        val progression = getPlayerProgressionData(player)!!
        val stack = event.item.item
        if (isItemRegisteredAsOre(stack, "logWood") && progression.givePlayerProgression("obtain_wood"))
            progressionMessage(player, "obtain_wood")
        if (itemsMatch(stack, ItemStack(Items.WHEAT, 1, OreDictionary.WILDCARD_VALUE)) &&
                progression.givePlayerProgression("obtain_wheat"))
            progressionMessage(player, "obtain_wheat")
        if (itemsMatch(stack, ItemStack(Items.POTATO, 1, OreDictionary.WILDCARD_VALUE)) &&
                progression.givePlayerProgression("obtain_potato"))
            progressionMessage(player, "obtain_potato")
        if (itemsMatch(stack, ItemStack(Items.CARROT, 1, OreDictionary.WILDCARD_VALUE)) &&
                progression.givePlayerProgression("obtain_carrot"))
            progressionMessage(player, "obtain_carrot")
        if (itemsMatch(stack, ItemStack(Items.PORKCHOP, 1, OreDictionary.WILDCARD_VALUE)) &&
                progression.givePlayerProgression("obtain_pork_chop"))
            progressionMessage(player, "obtain_pork_chop")
        if (itemsMatch(stack, ItemStack(Items.BEEF, 1, OreDictionary.WILDCARD_VALUE)) &&
                progression.givePlayerProgression("obtain_steak"))
            progressionMessage(player, "obtain_steak")
        if (itemsMatch(stack, ItemStack(Items.CHICKEN, 1, OreDictionary.WILDCARD_VALUE)) &&
                progression.givePlayerProgression("obtain_chicken"))
            progressionMessage(player, "obtain_chicken")
        if (itemsMatch(stack, ItemStack(Items.RABBIT, 1, OreDictionary.WILDCARD_VALUE)) &&
                progression.givePlayerProgression("obtain_rabbit"))
            progressionMessage(player, "obtain_rabbit")
        if (itemsMatch(stack, ItemStack(Items.FISH, 1, OreDictionary.WILDCARD_VALUE)) &&
                progression.givePlayerProgression("obtain_fish"))
            progressionMessage(player, "obtain_fish")
        if (isItemRegisteredAsOre(stack, "oreIron") && progression.givePlayerProgression("obtain_iron_ore"))
            progressionMessage(player, "obtain_iron_ore")
        if (isItemRegisteredAsOre(stack, "gemDiamond") && progression.givePlayerProgression("obtain_diamonds"))
            progressionMessage(player, "obtain_diamonds")
        if (isItemRegisteredAsOre(stack, "gemEmerald") && progression.givePlayerProgression("obtain_emerald"))
            progressionMessage(player, "obtain_emerald")
        if (isItemRegisteredAsOre(stack, "obsidian") && progression.givePlayerProgression("obtain_obsidian"))
            progressionMessage(player, "obtain_obsidian")
        if (itemsMatch(stack, ItemStack(Items.BLAZE_ROD, 1, OreDictionary.WILDCARD_VALUE)) &&
                progression.givePlayerProgression("obtain_blaze_rod"))
            progressionMessage(player, "obtain_blaze_rod")
        if (itemsMatch(stack, ItemStack(Items.SKULL, 1, 1)) && progression.givePlayerProgression("obtain_nether_skull"))
            progressionMessage(player, "obtain_nether_skull")
        if (itemsMatch(stack, ItemStack(Blocks.SOUL_SAND, 1, OreDictionary.WILDCARD_VALUE)) &&
                progression.givePlayerProgression("obtain_soul_sand"))
            progressionMessage(player, "obtain_soul_sand")
        if (isItemRegisteredAsOre(stack, "netherStar") && progression.givePlayerProgression("obtain_nether_star"))
            progressionMessage(player, "obtain_nether_star")
        if (itemsMatch(stack, ItemStack(Items.ELYTRA, 1, OreDictionary.WILDCARD_VALUE)) &&
                progression.givePlayerProgression("obtain_elytra"))
            progressionMessage(player, "obtain_elytra")
        if (itemsMatch(stack, ItemStack(Items.TOTEM_OF_UNDYING, 1, OreDictionary.WILDCARD_VALUE)) &&
                progression.givePlayerProgression("obtain_totem_death"))
            progressionMessage(player, "obtain_totem_death")

    }

    @SubscribeEvent(priority = EventPriority.LOWEST) fun entityDeathEvent(event: LivingDeathEvent) {
        if (event.isCanceled || !TrophySlots.useProgressionUnlocks) return
        val source = event.source
        if (source.trueSource !is EntityPlayer) return
        val player = source.trueSource as EntityPlayer
        val progress = getPlayerProgressionData(player)!!
        val entity = event.entityLiving
        if (entity is EntityEnderman && entity.dimension == 1 && progress.givePlayerProgression("kill_enderman"))
            progressionMessage(player, "kill_enderman")
        if (entity is EntityDragon && entity.dimension == 1 && progress.givePlayerProgression("kill_ender_dragon"))
            progressionMessage(player, "kill_ender_dragon")
        if (entity is EntityPlayer && entity != player && !isFakePlayer(entity) &&
                progress.givePlayerProgression("kill_player"))
            progressionMessage(player, "kill_player")
        if (player.health == 1f && progress.givePlayerProgression("one_heart")) progressionMessage(player, "one_heart")
    }

    @SubscribeEvent(priority = EventPriority.LOWEST) fun travelDimensionEvent(event: PlayerEvent.PlayerChangedDimensionEvent) {
        if (event.isCanceled || !TrophySlots.useProgressionUnlocks) return
        val player = event.player
        val progress = getPlayerProgressionData(player)!!
        if (event.toDim == -1 && event.toDim != event.fromDim && progress.givePlayerProgression("enter_nether"))
            progressionMessage(player, "enter_nether")
        if (event.toDim == 1 && event.toDim != event.fromDim && progress.givePlayerProgression("enter_end"))
            progressionMessage(player, "enter_end")
    }

    @SubscribeEvent(priority = EventPriority.LOWEST) fun playerPlacedBlock(event: BlockEvent.PlaceEvent) {
        if (event.isCanceled || !TrophySlots.useProgressionUnlocks) return
        val player = event.player
        val progress = getPlayerProgressionData(player)!!
        val block = event.placedBlock.block
        if ((block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST || block == Blocks.ENDER_CHEST) &&
                progress.givePlayerProgression("place_chest"))
            progressionMessage(player, "place_chest")
    }

    @SubscribeEvent fun restoreProgressionAfterDeath(event: net.minecraftforge.event.entity.player.PlayerEvent.Clone) {
        val progression = getPlayerProgressionData(event.entityPlayer)
        val oldProgression = getPlayerProgressionData(event.original)
        oldProgression!!.getUnlockList().stream().forEach { id -> progression?.forceAddProgression(id) }
    }

    @SubscribeEvent fun syncClientOnJoin(event: PlayerEvent.PlayerLoggedInEvent) {
        if (event.player == null) return
        val playerMP = FMLCommonHandler.instance().minecraftServerInstance.playerList.getPlayerByUUID(event.player.uniqueID)
        var progression = getPlayerProgressionData(playerMP)
        if (progression != null && progression.getUnlockList().isNotEmpty())
            TrophySlots.packetHandler?.sendTo(MessageUpdateClientProgress(progression.getUnlockList()), playerMP)
    }

    fun itemsMatch(stack1: ItemStack, stack2: ItemStack): Boolean {
        if ((stack1.isEmpty && !stack2.isEmpty) || (!stack1.isEmpty && stack2.isEmpty)) return false
        if (stack1.isEmpty && stack2.isEmpty) return true
        if (stack1.item == stack2.item) {
            if (stack1.metadata == OreDictionary.WILDCARD_VALUE || stack2.metadata == OreDictionary.WILDCARD_VALUE)
                return true
            return stack1.metadata == stack2.metadata
        }
        return false
    }

    fun isItemRegisteredAsOre(stack: ItemStack, oreName: String): Boolean {
        if (stack.isEmpty) return false
        val itemList = OreDictionary.getOres(oreName)
        for (item in itemList) {
            if (itemsMatch(item, stack)) return true
        }
        return false
    }

    private val FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$")

    fun isFakePlayer(player: EntityPlayer?): Boolean = if (player != null) player !is EntityPlayerMP || player is FakePlayer || FAKE_PLAYER_PATTERN.matcher(player.name).matches() else false
}