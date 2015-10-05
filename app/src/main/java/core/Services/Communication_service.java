package core.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Communication_service extends Service {
    public Communication_service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
