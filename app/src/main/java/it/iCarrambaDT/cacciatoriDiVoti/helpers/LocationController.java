package it.iCarrambaDT.cacciatoriDiVoti.helpers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;


public class LocationController {

    private Location currentLocation = null;
    private LocationManager locationManager;
    private LocationListener ll;
    private LocationUser lu;

    public void startListening(Context context, LocationUser lu) {

        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.lu = lu;

        this.ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                aggiornaLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //Controllo se ho i permessi necessari
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15, 5, ll);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15, 5, ll);

    }

    public void stopListening() {
        locationManager.removeUpdates(ll);
    }

    private void aggiornaLocation(Location location) {
        if (isBetterLocation(location,currentLocation)) {
            currentLocation = location;
            lu.locationArrived(location);
        }
    }


    private static final int ONE_MINUTES = 1000 * 60;

    //Controlla se la nuova location è meglio della precedente
    private boolean isBetterLocation(Location location, Location currentBestLocation) {

        //se non ho ancora una location prendo la nuova
        if (currentBestLocation == null) {
            return true;
        }
        //Se è passato un minuto dall'ultima location prendo la nuova
        //Se la nuova è più vecchia di un minuto mi tengo la vecchio
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > ONE_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -ONE_MINUTES;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Se la location ha un delta maggiore di 60 metri la scarto
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 60;

        //Controllo se hanno lo stesso provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        //Decido un base ai parametri se accettarla
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }



}
