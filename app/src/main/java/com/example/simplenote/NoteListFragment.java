package com.example.simplenote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simplenote.databinding.FragmentNoteListBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class NoteListFragment extends Fragment {

    private FragmentNoteListBinding binding;

    private NoteAdapter noteAdapter;
    private NoteListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNoteListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(NoteListViewModel.class);
        viewModel.getNotes().observe(getViewLifecycleOwner(), notes -> {
            noteAdapter = new NoteAdapter(notes, Navigation.findNavController(requireView()), viewModel);
            binding.noteListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.noteListRecyclerView.setAdapter(noteAdapter);
        });

        FloatingActionButton fab = view.findViewById(R.id.addNote);
        fab.setOnClickListener(v -> {
            viewModel.setCurrentId("");
            Navigation.findNavController(view).navigate(R.id.action_noteListFragment_to_noteDetailFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
