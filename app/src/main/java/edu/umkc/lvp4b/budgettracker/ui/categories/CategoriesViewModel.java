package edu.umkc.lvp4b.budgettracker.ui.categories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.umkc.lvp4b.budgettracker.data.AppDatabase;
import edu.umkc.lvp4b.budgettracker.data.CategoryEntity;

public class CategoriesViewModel extends AndroidViewModel {

    private MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final AppDatabase database;
    private Category selectedCategory;

    public CategoriesViewModel(Application application){
        super(application);
        categories.setValue(new ArrayList<>());
        database = AppDatabase.getInstance(application.getApplicationContext());
        new Init(this, database).execute();
    }

    private static class Init extends AsyncTask<Object, Object, Void>{
        private final CategoriesViewModel categoriesViewModel;
        private final AppDatabase database;

        private Init(CategoriesViewModel categoriesViewModel, AppDatabase database) {
            this.categoriesViewModel = categoriesViewModel;
            this.database = database;
        }

        @Override
        protected Void doInBackground(Object... objects) {
            categoriesViewModel.setCategories(database.categoryDao().getAll().stream()
                    .map(Category::fromEntity).collect(Collectors.toList()));
            return null;
        }
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> values){
        categories.postValue(values);
    }

    public Category getSelectedCategory(){
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory){
        this.selectedCategory = selectedCategory;
    }

    public void updateCategory(Category category){
        new UpdateTask(database).execute(category);
    }

    private static class UpdateTask extends AsyncTask<Category, Void, Void> {
        private final AppDatabase database;

        private UpdateTask(AppDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            for (Category category : categories) {
                database.categoryDao().update(category.toEntity());
            }
            return null;
        }
    }

    public int deleteCategory(Category category){
        final List<Category> values = new ArrayList<>(categories.getValue());
        final int position = values.indexOf(category);
        values.remove(category);
        new DeleteTask(database).execute(category);
        categories.setValue(values);
        return position;
    }

    private static class DeleteTask extends AsyncTask<Category, Void, Void> {
        private final AppDatabase database;

        private DeleteTask(AppDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            for (Category category : categories) {
                database.categoryDao().delete(category.toEntity());
            }
            return null;
        }
    }

    public void addCategory(Category category) {
        final List<Category> values = new ArrayList<>(categories.getValue());
        values.add(category);
        new InsertTask(database, category).execute(category);
        categories.setValue(values);
    }

    private class InsertTask extends AsyncTask<Category, Void, CategoryEntity> {
        private final AppDatabase database;
        private final Category category;

        private InsertTask(AppDatabase database, Category category) {
            this.database = database;
            this.category = category;
        }

        @Override
        protected CategoryEntity doInBackground(Category... categories) {
            CategoryEntity entity = categories[0].toEntity();
            category.setId((int)database.categoryDao().insert(entity));
            return entity;
        }

        @Override
        protected void onPostExecute(CategoryEntity entity) {
        }
    }
}
