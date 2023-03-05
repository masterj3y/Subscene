package github.masterj3y.data.extractor

interface Extractor<R> {

    fun extract(string: String?): R
}