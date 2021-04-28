import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Locale
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit

internal class CountriesServiceSolved : CountriesService {
    override fun countryNameInCapitals(country: Country): Single<String> {
        return Single.just(country.name)
            .map { it.toUpperCase(Locale.US) }
    }

    override fun countCountries(countries: List<Country>): Single<Long> {
        return Observable.fromIterable(countries)
            .count()
    }

    override fun listPopulationOfEachCountry(countries: List<Country>): Observable<Long> {
        return Observable.fromIterable(countries)
            .map(Country::population)
    }

    override fun listNameOfEachCountry(countries: List<Country>): Observable<String> {
        return Observable.fromIterable(countries)
            .map(Country::name)
    }

    override fun listOnly3rdAnd4thCountry(countries: List<Country>): Observable<Country> {
        return Observable.fromIterable(countries)
            .skip(2)
            .take(2)
    }

    override fun isAllCountriesPopulationMoreThanOneMillion(countries: List<Country>): Single<Boolean> {
        return Observable.fromIterable(countries)
            .all { it.population > 1000000 }
    }

    override fun listPopulationMoreThanOneMillion(countries: List<Country>): Observable<Country> {
        return Observable.fromIterable(countries)
            .filter { it.population > 1000000 }
    }

    override fun listPopulationMoreThanOneMillionWithTimeoutFallbackToEmpty(countriesFromNetwork: FutureTask<List<Country>>): Observable<Country> {
        return Observable.fromFuture(countriesFromNetwork, Schedulers.io())
            .flatMapIterable { it }
            .filter { it.population > 1000000 }
            .timeout(1, TimeUnit.SECONDS)
            .onErrorResumeNext { Observable.empty() }
    }

    override fun getCurrencyUSDIfNotFound(countryName: String, countries: List<Country>): Observable<String> {
        return Observable.fromIterable(countries)
            .filter { it.name == countryName }
            .map { it.currency }
            .defaultIfEmpty("USD")
    }

    override fun sumPopulationOfCountries(countries: List<Country>): Observable<Long> {
        return Observable.fromIterable(countries)
            .map(Country::population)
            .reduce(0L) { x, y -> x + y }.toObservable()
    }

    override fun sumPopulationOfCountries(
        countryObservable1: Observable<Country>,
        countryObservable2: Observable<Country>
    ): Observable<Long> {
        return Observable.concat(countryObservable1, countryObservable2)
            .map(Country::population)
            .reduce(0L) { x, y -> x + y }.toObservable()
    }

    override fun mapCountriesToNamePopulation(countries: List<Country>): Single<Map<String, Long>> {
        return Observable.fromIterable(countries)
            .toMap(
                { it.name },
                { it.population }
            )
    }

    override fun areEmittingSameSequences(
        countryObservable1: Observable<Country>,
        countryObservable2: Observable<Country>
    ): Single<Boolean> = Observable.sequenceEqual(countryObservable1, countryObservable2)
}
