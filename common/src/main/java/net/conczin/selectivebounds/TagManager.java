package net.conczin.selectivebounds;

import net.conczin.selectivebounds.config.Config;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TagManager {
    private final Config config;

    private final Map<String, Set<Item>> tagToItemsCache = new HashMap<>();
    private final Map<String, Set<Block>> tagToBlocksCache = new HashMap<>();

    private final Map<String, FilteredType> cache = new HashMap<>();

    public enum FilteredType {
        BLACKLIST,
        WHITELIST,
        IGNORE
    }

    public TagManager(Config config) {
        this.config = config;
    }

    public Set<Item> getItemsFromTag(String tag) {
        if (tagToItemsCache.containsKey(tag)) {
            return tagToItemsCache.get(tag);
        }

        Identifier location = Identifier.parse(tag.startsWith("#") ? tag.substring(1) : tag);
        TagKey<Item> tagKey = TagKey.create(BuiltInRegistries.ITEM.key(), location);
        Set<Item> items = new HashSet<>();
        for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tagKey)) {
            items.add(holder.value());
        }

        tagToItemsCache.put(tag, items);
        return items;
    }

    public Set<Block> getBlocksFromTag(String tag) {
        if (tagToBlocksCache.containsKey(tag)) {
            return tagToBlocksCache.get(tag);
        }

        Identifier location = Identifier.parse(tag.startsWith("#") ? tag.substring(1) : tag);
        TagKey<Block> tagKey = TagKey.create(BuiltInRegistries.BLOCK.key(), location);
        Set<Block> blocks = new HashSet<>();
        for (Holder<Block> holder : BuiltInRegistries.BLOCK.getTagOrEmpty(tagKey)) {
            blocks.add(holder.value());
        }
        tagToBlocksCache.put(tag, blocks);
        return blocks;
    }

    public FilteredType get(Item item, Block block) {
        Identifier itemKey = BuiltInRegistries.ITEM.getKey(item);
        Identifier blockKey = BuiltInRegistries.BLOCK.getKey(block);
        String key = itemKey + "$" + blockKey;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        FilteredType type = FilteredType.IGNORE;
        for (Config.OutlineOverride override : config.overrides) {
            if (override.item.equals("all") || override.item.equals(itemKey.toString()) || getItemsFromTag(override.item).contains(item)) {
                if (override.block.equals("all") || override.block.equals(blockKey.toString()) || getBlocksFromTag(override.block).contains(block)) {
                    type = override.show ? FilteredType.WHITELIST : FilteredType.BLACKLIST;
                    break;
                }
            }
        }

        cache.put(key, type);
        return type;
    }
}
