/*******************************************************************************
 * Copyright 2018 T145
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package T145.metalchests.lib.containers;

import net.minecraftforge.items.IItemHandler;

public class SlotRange {

    public final int first;
    public final int lastInc;
    public final int lastExc;

    public SlotRange(IItemHandler inv) {
        this(0, inv.getSlots());
    }

    public SlotRange(int start, int numSlots) {
        first = start;
        lastInc = start + numSlots - 1;
        lastExc = start + numSlots;
    }

    public boolean contains(int slot) {
        return slot >= first && slot <= lastInc;
    }

    @Override
    public String toString() {
        return String.format("SlotRange: {first: %d, lastInc: %d, lastExc: %d}", first, lastInc, lastExc);
    }
}
