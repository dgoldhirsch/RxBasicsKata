import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.jupiter.api.Test
import java.util.* // ktlint-disable no-wildcard-imports
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit

const val CURRENCY_EUR = "EUR"
const val CURRENCY_PLN = "PLN"
const val CURRENCY_GBP = "GBP"
const val CURRENCY_UAH = "UAH"
const val CURRENCY_CHF = "CHF"

class CountriesServiceSolvedTest {
    private val germany = Country("Germany", CURRENCY_EUR, 80620000)
    private val france = Country("France", CURRENCY_EUR, 66030000)
    private val unitedKingdom = Country("United Kingdom", CURRENCY_GBP, 64100000)
    private val poland = Country("Poland", CURRENCY_PLN, 38530000)
    private val ukraine = Country("Ukraine", CURRENCY_UAH, 45490000)
    private val austria = Country("Austria", CURRENCY_EUR, 8474000)
    private val switzerland = Country("Switzerland", CURRENCY_CHF, 8081000)
    private val luxembourg = Country("Luxembourg", CURRENCY_EUR, 576249)

    private val allCountries = listOf(
        germany,
        france,
        unitedKingdom,
        poland,
        ukraine,
        austria,
        switzerland,
        luxembourg
    )

    private var countriesService = CountriesServiceSolved()

    @Test
    fun rx_CountryNameInCapitals() {
        val testCountry = allCountries.first()

        val testObserver: TestObserver<String> = countriesService
            .countryNameInCapitals(testCountry)
            .test()

        testObserver.assertNoErrors()
        testObserver.assertValue(testCountry.name.toUpperCase(Locale.US))
    }

    @Test
    fun rx_CountCountries() {
        val testObserver: TestObserver<Long> = countriesService
            .countCountries(allCountries)
            .test()

        testObserver.assertNoErrors()
        testObserver.assertValue(allCountries.size.toLong())
    }

    @Test
    fun rx_ListPopulationOfEachCountry() {
        val testObserver = countriesService
            .listPopulationOfEachCountry(allCountries)
            .test()

        testObserver.assertValueSequence(allCountries.map(Country::population))
        testObserver.assertNoErrors()
    }

    @Test
    fun rx_ListNameOfEachCountry() {
        val testObserver = countriesService
            .listNameOfEachCountry(allCountries)
            .test()

        testObserver.assertValueSequence(allCountries.map(Country::name))
        testObserver.assertNoErrors()
    }

    @Test
    fun rx_ListOnly3rdAnd4thCountry() {
        val expectedResult: MutableList<Country?> = ArrayList()
        expectedResult.add(allCountries[2])
        expectedResult.add(allCountries[3])

        val testObserver = countriesService
            .listOnly3rdAnd4thCountry(allCountries)
            .test()

        testObserver.assertValueSequence(listOf(allCountries[2], allCountries[3]))
        testObserver.assertNoErrors()
    }

    @Test
    fun rx_IsAllCountriesPopulationMoreThanOneMillion_Positive() {
        val testObserver: TestObserver<Boolean> = countriesService
            .isAllCountriesPopulationMoreThanOneMillion(allCountries.filterNot { it == luxembourg })
            .test()

        testObserver.assertResult(true)
        testObserver.assertNoErrors()
    }

    @Test
    fun rx_IsAllCountriesPopulationMoreThanOneMillion_Negative() {
        val testObserver: TestObserver<Boolean> = countriesService
            .isAllCountriesPopulationMoreThanOneMillion(allCountries)
            .test()

        testObserver.assertResult(false)
        testObserver.assertNoErrors()
    }

    @Test
    fun rx_ListPopulationMoreThanOneMillion() {
        val testObserver = countriesService
            .listPopulationMoreThanOneMillion(allCountries)
            .test()

        testObserver.assertValueSequence(allCountries.filterNot { it == luxembourg })
        testObserver.assertNoErrors()
    }

    @Test // Added by me
    fun rx_ListPopulationMoreThanOneMillion_GivenEmptyList() {
        val testObserver = countriesService
            .listPopulationMoreThanOneMillion(emptyList())
            .test()

        testObserver.assertValueSequence(emptyList())
        testObserver.assertNoErrors()
    }

    @Test // Added by me
    fun rx_ListPopulationMoreThanOneMillion_GivenThatNoneQualify() {
        val testObserver = countriesService
            .listPopulationMoreThanOneMillion(listOf(luxembourg))
            .test()

        testObserver.assertValueSequence(emptyList())
        testObserver.assertNoErrors()
    }

    @Test
    @Throws(InterruptedException::class)
    fun rx_ListPopulationMoreThanOneMillionWithTimeoutFallbackToEmpty_When_NoTimeout() {
        val futureTask = FutureTask {
            TimeUnit.MILLISECONDS.sleep(100)
            allCountries
        }

        Thread(futureTask).start()

        val testObserver = countriesService
            .listPopulationMoreThanOneMillionWithTimeoutFallbackToEmpty(futureTask)
            .test()

        testObserver.await()
        testObserver.assertComplete()
        testObserver.assertValueSequence(allCountries.filterNot { it == luxembourg })
        testObserver.assertNoErrors()
    }

    @Test
    @Throws(InterruptedException::class)
    fun rx_ListPopulationMoreThanOneMillionWithTimeoutFallbackToEmpty_When_Timeout() {
        val futureTask = FutureTask {
            TimeUnit.SECONDS.sleep(20)
            allCountries
        }

        Thread(futureTask).start()

        val testObserver: TestObserver<Country> = countriesService
            .listPopulationMoreThanOneMillionWithTimeoutFallbackToEmpty(futureTask)
            .test()

        testObserver.await()
        testObserver.assertComplete()
        testObserver.assertNoValues()
        testObserver.assertNoErrors()
    }

    @Test
    fun rx_GetCurrencyUSDIfNotFound_When_CountryFound() {
        val countryName = "Austria"
        val expectedCurrency = "EUR"

        val testObserver: TestObserver<String> = countriesService
            .getCurrencyUSDIfNotFound(countryName, allCountries)
            .test()

        testObserver.assertResult(expectedCurrency)
        testObserver.assertNoErrors()
    }

    @Test
    fun rx_GetCurrencyUSDIfNotFound_When_CountryNotFound() {
        val countryName = "Senegal"
        val expectedCurrency = "USD"

        val testObserver: TestObserver<String> = countriesService
            .getCurrencyUSDIfNotFound(countryName, allCountries)
            .test()

        testObserver.assertResult(expectedCurrency)
        testObserver.assertNoErrors()
    }

    @Test
    fun rx_sumPopulationOfCountries() {
        // hint: use "reduce" operator
        val testObserver = countriesService
            .sumPopulationOfCountries(allCountries)
            .test()

        testObserver.assertResult(allCountries.map(Country::population).sum())
        testObserver.assertNoErrors()
    }

    @Test
    fun rx_sumPopulationOfCountries_inputsAreObservables() {
        // hint: use "map" operator
        val testObserver: TestObserver<Long> = countriesService
            .sumPopulationOfCountries(Observable.fromIterable(allCountries), Observable.fromIterable(allCountries))
            .test()

        testObserver.assertResult(2 * allCountries.map(Country::population).sum())
        testObserver.assertNoErrors()
    }

    @Test
    fun rx_MapCountriesToNamePopulation() {
        val values = countriesService.mapCountriesToNamePopulation(allCountries).test()

        values.assertResult(allCountries.map { it.name to it.population }.toMap())
        values.assertNoErrors()
    }

    @Test
    fun rx_areEmittingSameSequences_Positive() {
        // hint: use "sequenceEqual" operator
        val testObserver: TestObserver<Boolean> = countriesService
            .areEmittingSameSequences(Observable.fromIterable(allCountries), Observable.fromIterable(allCountries))
            .test()

        testObserver.assertResult(true)
        testObserver.assertNoErrors()
    }

    @Test
    fun rx_areEmittingSameSequences_Negative() {
        val allCountriesDifferentSequence: List<Country?> = ArrayList(allCountries)
        Collections.swap(allCountriesDifferentSequence, 0, 1)

        val testObserver: TestObserver<Boolean> = countriesService
            .areEmittingSameSequences(
                Observable.fromIterable(allCountries),
                Observable.fromIterable(allCountriesDifferentSequence)
            )
            .test()

        testObserver.assertResult(false)
        testObserver.assertNoErrors()
    }
}
