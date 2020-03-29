package edu.umkc.lvp4b.budgettracker.ui.transaction;

import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

import edu.umkc.lvp4b.budgettracker.R;
import edu.umkc.lvp4b.budgettracker.databinding.FragmentCameraBinding;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback, Camera.PictureCallback {
    private FragmentCameraBinding binding;
    private Camera camera;
    private SurfaceHolder holder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(inflater);

        final TransactionsViewModel transactionsViewModel =
                ViewModelProviders.of(requireActivity()).get(TransactionsViewModel.class);

        binding.setTransaction(transactionsViewModel.getSelectedTransaction());

        binding.capture.setOnClickListener(view -> {
            Optional.ofNullable(camera).ifPresent(c -> c.takePicture(null, null, null, this));
        });


        holder = binding.surfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        return binding.getRoot();
    }

    private void setCamera(Camera camera) {
        try {
            if (this.camera == camera) {
                return;
            } else if (this.camera != null) {
                this.camera.stopPreview();
                this.camera.release();
            }

            this.camera = camera;

            if (camera == null) {
                return;
            }

            getView().requestLayout();
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            setCamera(Camera.open());
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera == null) {
            return;
        }

        Camera.Parameters parameters = camera.getParameters();
        parameters.setJpegQuality(80);
        getView().requestLayout();
        camera.setParameters(parameters);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Optional.ofNullable(camera).ifPresent(Camera::stopPreview);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        binding.getTransaction().setImage(BitmapFactory.decodeByteArray(data, 0, data.length));
        Navigation.findNavController(getView()).navigateUp();
    }
}
