# Owners and thieves

## Description

**Owners and thieves** - multithreading application, there is one apartment with some owners,
who are adding items to their apartment, and some thieves, who are trying to steal these items.

Every thief has his own backpack with limit weight.
Thief must take such items so that their value is maximized
and their weight must be less or equal to the weight that can fit in the backpack.  

Every owner and every thief are separate threads, but there are few important conditions:
* Owners don't block each over, that means that few owners can be inside apartment at the same time;
* While at least one owner is in apartment, thieves can't come to the apartment;
* Every thief blocks over thieves and all owners to come inside apartment,
that means that inside the apartment may be only one thief at the same time,
and owners can't come to apartment before thief leaves it.

You can change some parameters to make your simulation more interesting and unique, here they are:
* `OWNERS_COUNT` - count of owners threads;
* `THIEVES_COUNT` - count of thieves threads;
* `MAX_ITEM_VALUE` - max value of generated items;
* `MAX_ITEM_WEIGHT` - max weight of generated items;
* `MAX_ITEMS_COUNT` - max count of generated items for every separate owner;
* `MAX_BACKPACK_WEIGHT` - max weight for generated backpack for every separate thief.

Now try it by yourself and have fun.