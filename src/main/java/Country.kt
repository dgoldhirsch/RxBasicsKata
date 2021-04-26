internal class Country(val name: String, val currency: String, val population: Long) {
    override fun toString(): String {
        return "Country{" +
            "name='" + name + '\'' +
            ", currency='" + currency + '\'' +
            ", population=" + population +
            '}'
    }
}
