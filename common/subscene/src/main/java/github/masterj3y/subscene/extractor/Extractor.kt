package github.masterj3y.subscene.extractor

interface Extractor<R> {

    fun extract(string: String?): R
}