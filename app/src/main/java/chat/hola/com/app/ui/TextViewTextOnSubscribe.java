package chat.hola.com.app.ui;

import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import chat.hola.com.app.Utilities.Utilities;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicBoolean;

class TextViewTextOnSubscribe implements ObservableOnSubscribe<CharSequence> {
  private final TextView view;

  TextViewTextOnSubscribe(TextView view) {
    this.view = view;
  }

  @Override
  public void subscribe(final ObservableEmitter<CharSequence> emitter) throws Exception {
    Utilities.printLog("emiting searching ");
    final TextWatcher watcher = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        Utilities.printLog("emiting searching is " + emitter.isDisposed());

        if (!emitter.isDisposed()) {
          emitter.onNext(s);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    };

    emitter.setDisposable(new MainThreadDisposable() {
      @Override
      protected void onDisposed() {
        view.removeTextChangedListener(watcher);
      }
    });

    view.addTextChangedListener(watcher);

    // Emit initial value.
    emitter.onNext(view.getText());
  }

  abstract class MainThreadDisposable implements Disposable {

    private final AtomicBoolean dispose = new AtomicBoolean();

    @Override
    public void dispose() {
      Utilities.printLog("emiting searching is di ");

      if (dispose.compareAndSet(false, true)) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
          onDisposed();
        } else {
          AndroidSchedulers.mainThread()
              .createWorker()
              .schedule(new Runnable() {
                @Override
                public void run() {
                  onDisposed();
                }
              });
        }
      }
    }

    @Override
    public boolean isDisposed() {
      return dispose.get();
    }

    abstract void onDisposed();
  }
}
