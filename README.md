===============================================================================
                     VIRTUAL LAUNCHPAD APPLICATION
===============================================================================

This software project consists of a customizable virtual Launchpad application
developed in Java. The system was designed and implemented as an academic
project by the developers Enzo Mardegan Gallego and Henrique Ferreira Festraits.

-------------------------------------------------------------------------------
1. REQUIREMENTS
-------------------------------------------------------------------------------
To run this application, the following software and resources are required:

- Java Runtime Environment (JRE) or Java Development Kit (JDK) version 8
  or higher.
- An active Internet connection (required for authentication and saving
  Presets to the external database).
- The "Assets" folder must remain in the same directory as the executable
  file, as it contains all audio resources required by the application.

-------------------------------------------------------------------------------
2. HOW TO RUN THE APPLICATION
-------------------------------------------------------------------------------
If you downloaded the compressed package, extract all files into a folder.

IMPORTANT:
Do not remove, rename, or move the "Assets" folder. The application depends
on these files for audio playback.

Expected directory structure:

    Assets/
    Launchpad.exe
    README.txt

On Windows:

- Double-click the executable file:

    Launchpad.exe

- Or open Command Prompt (CMD), navigate to the application folder and run:

    Launchpad.exe

Linux / macOS:

This version is currently distributed only as a Windows executable (.exe).

-------------------------------------------------------------------------------
3. USER INSTRUCTIONS
-------------------------------------------------------------------------------

WINDOW MOVEMENT

The application uses a custom borderless interface design. To move the
window anywhere on the screen, click and drag using the custom title bar
located at the top.

AUTHENTICATION

Access to the system requires user registration and authentication using
the Academic Registration Number (R.A.).

PRESETS

After authentication, users can:

- Create Presets
- Save configurations
- Load existing profiles
- Rename Presets
- Delete Presets

Each configuration stores custom sound mappings and DrumKit assignments.

SOUND TRIGGERS

Sounds can be triggered either by:

- Clicking pads using the mouse
- Pressing mapped keyboard keys

The audio engine supports:

- Up to 4 simultaneous voices (polyphony)
- Overlapping playback
- Low-latency triggering
- Loop playback support for selected sounds

-------------------------------------------------------------------------------
4. SYSTEM FEATURES
-------------------------------------------------------------------------------

Audio System

- Real-time audio triggering
- Keyboard and mouse interaction
- Polyphonic playback engine
- Audio preloading into RAM
- Overlapping playback support
- Loop playback functionality
- External WAV audio integration

User System

- Registration and authentication
- Academic R.A. identification system
- Persistent user management

Preset Management

Users can create and manage personalized DrumKit profiles and save them
directly into the external database.

Database Integration

The application supports complete CRUD persistence for:

- User accounts
- Presets
- DrumKit configurations
- Saved user settings

-------------------------------------------------------------------------------
5. AUTHORS AND CREDITS
-------------------------------------------------------------------------------

Project fully developed by:

- Enzo Mardegan Gallego
- Henrique Ferreira Festraits

Academic Computer Engineering project.

===============================================================================
