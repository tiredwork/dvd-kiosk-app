package models

data class Item (var itemId: Int = 0, var itemContents : String, var isItemComplete: Boolean = false){

    override fun toString(): String {
        //TODO Lift Return out in labs
        if (isItemComplete)
            return "$itemId: $itemContents (Complete)"
         else
            return "$itemId: $itemContents (TODO)"
    }

}