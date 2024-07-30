package com.example.simplenote;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.simplenote.databinding.FragmentNoteDetailBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.Objects;

public class NoteDetailFragment extends Fragment {

    protected FragmentNoteDetailBinding binding;
    protected NoteListViewModel viewModel;
    protected Note note;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentNoteDetailBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(NoteListViewModel.class);

        return binding.getRoot();
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // back to note list
        binding.materialToolbar2.setNavigationOnClickListener( v -> {
            Navigation.findNavController(view).navigateUp();
        });

        // title
        String currentTitle = viewModel.getCurrentId();
        if (currentTitle.isEmpty()) {
            note = new Note("Untitled", "");
        } else {
            note = viewModel.getNoteById(currentTitle);

            binding.noteTextReadonly.setText(note.content);
            binding.noteTextReadonly.setVisibility(View.VISIBLE);
            binding.noteTextEditable.setVisibility(View.GONE);

            binding.materialToolbar2.getMenu().getItem(1).setVisible(true);
            binding.materialToolbar2.getMenu().getItem(0).setVisible(false);
        }
        binding.titleReadonly.setText(note.title);

        // handle title edit
        binding.titleReadonly.setOnClickListener(v -> {
            binding.titleEditable.setText(note.title);
            binding.titleReadonly.setVisibility(View.GONE);
            binding.titleEditable.setVisibility(View.VISIBLE);
        });
        binding.titleEditable.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!viewModel.getCurrentId().isEmpty()) {
                    // update title
                    if (viewModel.renameNote(note, String.valueOf(binding.titleEditable.getText()))){
                        Snackbar.make(binding.getRoot(), "Title updated", Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(R.color.md_theme_inversePrimary)
                                .show();
                    } else {
                        // if fail to rename, notify user
                        Snackbar.make(binding.getRoot(), "Fail to update title", Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(R.color.md_theme_error)
                                .show();
                    }

                }else {
                    note.title = String.valueOf(binding.titleEditable.getText());
                }

                binding.titleReadonly.setText(note.title);
                binding.titleReadonly.setVisibility(View.VISIBLE);
                binding.titleEditable.setVisibility(View.GONE);
            }
        });

        // menu
        Toolbar toolbar = binding.materialToolbar2;
        toolbar.setOnMenuItemClickListener( item -> {
            if (item.getItemId() == R.id.action_save) {
                // handle save action
                String content = String.valueOf(binding.noteTextEditable.getText());
                if (viewModel.getCurrentId().isEmpty()) {
                    note.content = content;
                    if (viewModel.addNote(note)) {
                        Snackbar.make(binding.getRoot(), "Note created", Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(R.color.colorCustomColor1)
                                .show();
                    }else {
                        // if fail to create file, notify user
                        Snackbar.make(binding.getRoot(), "Fail to create note", Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(R.color.md_theme_error).setBackgroundTintMode(PorterDuff.Mode.SRC)
                                .show();
                    }

                    viewModel.setCurrentId(note.title);
                }else if (!Objects.equals(note.content, content)) {
                    note.content = content;

                    if (viewModel.updateNote(note)) {
                        Snackbar.make(binding.getRoot(), "Note updated", Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(R.color.md_theme_inversePrimary)
                                .show();
                    }else {
                        // if fail to update file, notify user
                        Snackbar.make(binding.getRoot(), "Fail to update note", Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(R.color.md_theme_error)
                                .show();
                    }
                }

                toolbar.getMenu().getItem(1).setVisible(true);
                toolbar.getMenu().getItem(0).setVisible(false);

                binding.noteTextReadonly.setText(note.content);
                binding.noteTextEditable.setVisibility(View.GONE);
                binding.noteTextReadonly.setVisibility(View.VISIBLE);
            } else if (item.getItemId() == R.id.action_edit) {
                // handle edit action
                toolbar.getMenu().getItem(1).setVisible(false);
                toolbar.getMenu().getItem(0).setVisible(true);

                binding.noteTextEditable.setText(note.content);
                binding.noteTextReadonly.setVisibility(View.GONE);
                binding.noteTextEditable.setVisibility(View.VISIBLE);
                binding.noteTextEditable.requestFocus();
            }
            return false;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
