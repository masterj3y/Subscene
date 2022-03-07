package github.masterj3y.subscenecommon.extractor

interface Extractor<R> {

    fun extract(string: String?): R
}