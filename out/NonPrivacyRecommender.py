import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.preprocessing import StandardScaler
import os
import time

def load_and_preprocess_data(filepath):
    df = pd.read_csv(filepath)
    categorical_columns = ['GenderPreference', 'AgePreference', 'FoodPreference', 'Location', 'Race', 'IndoorActivity', 'OutdoorActivity']
    df = pd.get_dummies(df, columns=categorical_columns)
    scaler = StandardScaler()
    numerical_columns = ['FamilySize', 'Rationality', 'Budget', 'DurationOfStay', 'NumberOfPlaces']
    df[numerical_columns] = scaler.fit_transform(df[numerical_columns])
    return df

def calculate_similarity(df):
    return cosine_similarity(df)

def recommend_users(user_index, similarity_matrix, top_n=5):
    user_similarity_scores = similarity_matrix[user_index]
    top_users_indices = user_similarity_scores.argsort()[::-1][1:top_n+1]  # exclude the user itself
    top_users_scores = user_similarity_scores[top_users_indices]
    return top_users_indices, top_users_scores

def visualize_results(top_indices, top_scores, dataset_label, similarity_matrix):
    fig, axs = plt.subplots(1, 2, figsize=(12, 5))
    axs[0].bar(range(len(top_scores)), top_scores, color='skyblue')
    axs[0].set_title(f'Top 5 Recommendations for {dataset_label}')
    axs[0].set_xticks(range(len(top_scores)))
    axs[0].set_xticklabels(top_indices)
    axs[0].set_xlabel('User Indices')
    axs[1].imshow(similarity_matrix, cmap='hot', interpolation='nearest')
    axs[1].set_title(f'Similarity Matrix for {dataset_label}')
    plt.show()

def run_experiments(data_sizes, dataset_type):
    time_records = []
    for size in data_sizes:
        filepath = f'travel_data_{dataset_type}_{size}.csv'
        start_time = time.time()
        df = load_and_preprocess_data(filepath)
        similarity_matrix = calculate_similarity(df)
        top_indices, top_scores = recommend_users(0, similarity_matrix)  # Assuming user_index=0
        end_time = time.time()
        time_records.append((size, end_time - start_time))
        print(f'Dataset size: {size}, Processing time: {end_time - start_time:.2f} seconds')
        print(f'Top recommended users indices for the first user: {top_indices}')
        print(f'Top scores: {top_scores}')

    with open(f'recommendation_times_{dataset_type}.txt', 'w') as f:
        for record in time_records:
            f.write(f'{record[0]}, {record[1]}\n')

    sizes, times = zip(*time_records)
    plt.figure(figsize=(10, 5))
    plt.plot(sizes, times, marker='o', linestyle='-', color='b')
    plt.title(f'Recommendation Processing Time vs Data Size for {dataset_type}')
    plt.xlabel('Data Size')
    plt.ylabel('Time (seconds)')
    plt.grid(True)
    plt.savefig(f'timing_plot_{dataset_type}.png')
    plt.show()

if __name__ == "__main__":
    sizes = [1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000,
             11000, 12000, 13000, 14000, 15000, 16000, 17000, 18000, 19000,
             20000]
    run_experiments(sizes, 'gaussian')
    run_experiments(sizes, 'exponential')
