
package com.example.fitgenerator.models;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.parceler.Generated;
import org.parceler.IdentityCollection;
import org.parceler.ParcelWrapper;
import org.parceler.ParcelerRuntimeException;

@Generated("org.parceler.ParcelAnnotationProcessor")
@SuppressWarnings({
    "unchecked",
    "deprecation"
})
public class Fit$$Parcelable
    implements Parcelable, ParcelWrapper<com.example.fitgenerator.models.Fit>
{

    private com.example.fitgenerator.models.Fit fit$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Creator<Fit$$Parcelable>CREATOR = new Creator<Fit$$Parcelable>() {


        @Override
        public Fit$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new Fit$$Parcelable(read(parcel$$2, new IdentityCollection()));
        }

        @Override
        public Fit$$Parcelable[] newArray(int size) {
            return new Fit$$Parcelable[size] ;
        }

    }
    ;

    public Fit$$Parcelable(com.example.fitgenerator.models.Fit fit$$2) {
        fit$$0 = fit$$2;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$0, int flags) {
        write(fit$$0, parcel$$0, flags, new IdentityCollection());
    }

    public static void write(com.example.fitgenerator.models.Fit fit$$1, android.os.Parcel parcel$$1, int flags$$0, IdentityCollection identityMap$$0) {
        int identity$$0 = identityMap$$0 .getKey(fit$$1);
        if (identity$$0 != -1) {
            parcel$$1 .writeInt(identity$$0);
        } else {
            parcel$$1 .writeInt(identityMap$$0 .put(fit$$1));
        }
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public com.example.fitgenerator.models.Fit getParcel() {
        return fit$$0;
    }

    public static com.example.fitgenerator.models.Fit read(android.os.Parcel parcel$$3, IdentityCollection identityMap$$1) {
        int identity$$1 = parcel$$3 .readInt();
        if (identityMap$$1 .containsKey(identity$$1)) {
            if (identityMap$$1 .isReserved(identity$$1)) {
                throw new ParcelerRuntimeException("An instance loop was detected whild building Parcelable and deseralization cannot continue.  This error is most likely due to using @ParcelConstructor or @ParcelFactory.");
            }
            return identityMap$$1 .get(identity$$1);
        } else {
            com.example.fitgenerator.models.Fit fit$$4;
            int reservation$$0 = identityMap$$1 .reserve();
            fit$$4 = new com.example.fitgenerator.models.Fit();
            identityMap$$1 .put(reservation$$0, fit$$4);
            com.example.fitgenerator.models.Fit fit$$3 = fit$$4;
            identityMap$$1 .put(identity$$1, fit$$3);
            return fit$$3;
        }
    }

}
