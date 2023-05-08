package com.omniscient.omnibalance.Core.GUI;

import com.omniscient.omnibalance.Core.Items.ItemFactory;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class ListGUI extends OmniGUI {
    public int page = 0;
    public int maxPage = 0;
    public List<ItemStack> items;
    public int[] slots;
    public int previous;
    public int next;

    public ListGUI(List<ItemStack> items) {
        this.items = items;
        try {
            this.slots = ListGUI.class.getDeclaredMethod("onListItemClick", InventoryClickEvent.class).getAnnotation(Click.class).slots();
            this.previous = ListGUI.class.getDeclaredMethod("onPreviousPageClick", InventoryClickEvent.class).getAnnotation(Click.class).slots()[0];
            this.next = ListGUI.class.getDeclaredMethod("onNextPageClick", InventoryClickEvent.class).getAnnotation(Click.class).slots()[0];
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ItemStack> updateItems(){
        return items;
    }

    @Override
    public void update() {
        items = updateItems();
        super.update();
    }

    @Override
    public void makeItems(ItemStack[] items) {
        this.maxPage = (int) Math.floor((double) this.items.size() / slots.length);

        for (int i = page*slots.length; i < (page+1)*slots.length; i++) {
            if(i >= this.items.size()) return;
            int slot = slots[i-page*slots.length];
            ItemStack item = this.items.get(i-page*slots.length);
            items[slot] = item;
        }
        if(page > 0)
            items[previous] = ItemFactory.make("&ePrevious &8("+(page)+"/"+(maxPage+1)+")", "&7Click to go to the previous page.", Material.ARROW);
        if(page < maxPage)
            items[previous] = ItemFactory.make("&eNext &8("+(page+2)+"/"+(maxPage+1)+")", "&7Click to go to the next page.", Material.ARROW);
    }

    @Click(slots = {18}, actions = {
            InventoryAction.NOTHING,
            InventoryAction.PICKUP_ALL,
            InventoryAction.PICKUP_SOME,
            InventoryAction.PICKUP_HALF,
            InventoryAction.PICKUP_ONE,
            InventoryAction.PLACE_ALL,
            InventoryAction.PLACE_SOME,
            InventoryAction.PLACE_ONE,
            InventoryAction.SWAP_WITH_CURSOR,
            InventoryAction.DROP_ALL_CURSOR,
            InventoryAction.DROP_ONE_CURSOR,
            InventoryAction.DROP_ALL_SLOT,
            InventoryAction.DROP_ONE_SLOT,
            InventoryAction.MOVE_TO_OTHER_INVENTORY,
            InventoryAction.HOTBAR_MOVE_AND_READD,
            InventoryAction.HOTBAR_SWAP,
            InventoryAction.CLONE_STACK,
            InventoryAction.COLLECT_TO_CURSOR,
            InventoryAction.UNKNOWN
    })
    public void onPreviousPageClick(InventoryClickEvent e) {
        if(page > 0){
            page--;
            update();
        }
    }

    @Click(slots = {26}, actions = {
            InventoryAction.NOTHING,
            InventoryAction.PICKUP_ALL,
            InventoryAction.PICKUP_SOME,
            InventoryAction.PICKUP_HALF,
            InventoryAction.PICKUP_ONE,
            InventoryAction.PLACE_ALL,
            InventoryAction.PLACE_SOME,
            InventoryAction.PLACE_ONE,
            InventoryAction.SWAP_WITH_CURSOR,
            InventoryAction.DROP_ALL_CURSOR,
            InventoryAction.DROP_ONE_CURSOR,
            InventoryAction.DROP_ALL_SLOT,
            InventoryAction.DROP_ONE_SLOT,
            InventoryAction.MOVE_TO_OTHER_INVENTORY,
            InventoryAction.HOTBAR_MOVE_AND_READD,
            InventoryAction.HOTBAR_SWAP,
            InventoryAction.CLONE_STACK,
            InventoryAction.COLLECT_TO_CURSOR,
            InventoryAction.UNKNOWN
    })
    public void onNextPageClick(InventoryClickEvent e) {
        if(page < maxPage){
            page++;
            update();
        }
    }

    @Click(slots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    }, actions = {
            InventoryAction.NOTHING,
            InventoryAction.PICKUP_ALL,
            InventoryAction.PICKUP_SOME,
            InventoryAction.PICKUP_HALF,
            InventoryAction.PICKUP_ONE,
            InventoryAction.PLACE_ALL,
            InventoryAction.PLACE_SOME,
            InventoryAction.PLACE_ONE,
            InventoryAction.SWAP_WITH_CURSOR,
            InventoryAction.DROP_ALL_CURSOR,
            InventoryAction.DROP_ONE_CURSOR,
            InventoryAction.DROP_ALL_SLOT,
            InventoryAction.DROP_ONE_SLOT,
            InventoryAction.MOVE_TO_OTHER_INVENTORY,
            InventoryAction.HOTBAR_MOVE_AND_READD,
            InventoryAction.HOTBAR_SWAP,
            InventoryAction.CLONE_STACK,
            InventoryAction.COLLECT_TO_CURSOR,
            InventoryAction.UNKNOWN
    })
    public abstract void onListItemClick(InventoryClickEvent e);
}
