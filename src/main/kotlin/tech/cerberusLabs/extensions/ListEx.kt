package tech.cerberusLabs.extensions

fun List<String>.printRectangle() {
    val maxLen = this.maxOfOrNull { it.length }?.plus(7) ?: 0
    println("#".repeat(maxLen + 1))
    for ((index, text) in this.withIndex()) {
        val paddingSize = maxLen - text.length - 2
        val paddingStart = paddingSize / 2
        val paddingEnd = paddingSize - paddingStart
        when (index) {
            0 -> {
                println("# ${" ".repeat(paddingStart)}$text${" ".repeat(paddingEnd)}#")
            }
            this.lastIndex -> {
                println("#${" ".repeat(paddingStart)}$text${" ".repeat(paddingEnd)} #")
            }
            else -> {
                println("# ${" ".repeat(paddingStart)}$text${" ".repeat(paddingEnd)}#")
            }
        }
    }
    println("#".repeat(maxLen + 1))
}