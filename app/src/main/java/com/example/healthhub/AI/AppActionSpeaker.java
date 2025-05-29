package com.example.healthhub.AI;

/**
 * Interface to define common actions that an Activity/Fragment (UI component) can perform
 * to provide feedback to the user, driven by AI components.
 */
public interface AppActionSpeaker {
    /**
     * Displays a short toast message to the user.
     * @param message The message to display.
     */
    void showToast(String message);

    /**
     * Updates a primary status text view on the UI.
     * @param status The status message to display.
     */
    void updateStatus(String status);

    // You can add more methods here if needed, for example:
    // void navigateTo(Intent intent); // To allow handlers to trigger screen changes more generically
    // void playSound(int resId);
}