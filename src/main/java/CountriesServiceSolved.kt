import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.FutureTask

internal class CountriesServiceSolved : CountriesService {
    override fun countryNameInCapitals(country: Country): Single<String> {
        return Single.error(Exception())
    }

    override fun countCountries(countries: List<Country>): Single<Long> {
        return Single.error(Exception())
    }

    override fun listPopulationOfEachCountry(countries: List<Country>): Observable<Long> {
        return Observable.error(Exception())
    }

    override fun listNameOfEachCountry(countries: List<Country>): Observable<String> {
        return Observable.error(Exception())
    }

    override fun listOnly3rdAnd4thCountry(countries: List<Country>): Observable<Country> {
        return Observable.error(Exception())
    }

    override fun isAllCountriesPopulationMoreThanOneMillion(countries: List<Country>): Single<Boolean> {
        return Single.error(Exception())
    }

    override fun listPopulationMoreThanOneMillion(countries: List<Country>): Observable<Country> {
        return Observable.error(Exception())
    }

    override fun listPopulationMoreThanOneMillionWithTimeoutFallbackToEmpty(countriesFromNetwork: FutureTask<List<Country>>): Observable<Country> {
        return Observable.error(Exception())
    }

    override fun getCurrencyUSDIfNotFound(countryName: String, countries: List<Country>): Observable<String> {
        return Observable.error(Exception())
    }

    override fun sumPopulationOfCountries(countries: List<Country>): Observable<Long> {
        return Observable.error(Exception())
    }

    override fun sumPopulationOfCountries(countryObservable1: Observable<Country>, countryObservable2: Observable<Country>): Observable<Long> {
        return Observable.error(Exception())
    }

    override fun mapCountriesToNamePopulation(countries: List<Country>): Single<Map<String, Long>> {
        return Single.error(Exception())
    }

    override fun areEmittingSameSequences(countryObservable1: Observable<Country>, countryObservable2: Observable<Country>): Single<Boolean> {
        return Single.error(Exception())
    }
}
