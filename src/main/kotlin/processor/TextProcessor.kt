package processor

import java.io.File

/**
 * 过滤单词频率
 */
class TextProcessor {
    fun processText(text: String): List<WordFreq> {
//        return text
//            .clean()
//            .split(" ")
//            .getWordCount()
//            .mapToList { WordFreq(it.key, it.value) }
//            .sortedByDescending { it.frequency }
        return text
            .clean()
            .split(" ")
            .filter { it != " " }
            .groupBy { it }
            .map { WordFreq(it.key, it.value.size) }
            .sortedByDescending { it.frequency }
    }

    /**
     * 词频排序
     */
    private fun Map<String, Int>.sortByFrequency(): List<WordFreq> {
        val list = mutableListOf<WordFreq>()
        for (entry in this) {
            if (entry.key == " ") continue
            val freq = WordFreq(entry.key, entry.value)
            list.add(freq)
        }
        list.sortByDescending {
            it.frequency
        }
        return list
    }

    //inline关键字
    private inline fun <T> Map<String, Int>.mapToList(transform: (Map.Entry<String, Int>) -> T): MutableList<T> {
        val list = mutableListOf<T>()
        for (entry in this) {
            val freq = transform(entry)
            list.add(freq)
        }
        return list
    }

    /**
     * 统计单词频率
     */
    private fun List<String>.getWordCount(): Map<String, Int> {
        val map = hashMapOf<String, Int>()
        for (word in this) {
            if (word == " ") {
                continue
            }
            val trim = word.trim()
            val count = map.getOrDefault(trim, 0)
            map[trim] = count + 1
        }
        return map
    }

    /**
     * 清洗文本
     */
    private fun String.clean() =
        this.replace("[^A-Za-z]".toRegex(), "").trim()


    /**
     * 读取文件
     */
    fun processFile(file: File): List<WordFreq> {
        val text = file.readText(Charsets.UTF_8)
        return processText(text)
    }
}


data class WordFreq(val word: String, val frequency: Int)


