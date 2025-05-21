# Küberturvalisuse Simulaator - Simplified Implementation Plan

This implementation plan focuses on efficiently extending the current text-based simulator to add JavaFX functionality while maintaining the existing code structure as much as possible. The goal is to meet all stage 2 requirements with minimal refactoring.

## 1. Project Adaptation

### 1.1. JavaFX Setup
- Add JavaFX library to the project
- Create a new main class `KüberSimulaatorFX` that will serve as the JavaFX entry point
- Keep existing classes intact and extend functionality where needed

### 1.2. Minimal Code Adjustment
- Retain existing model classes (Stsenaarium, Küsimus, Valik) without major changes
- Add new UI classes for JavaFX without disrupting the existing functionality
- Create simple adapter methods where needed to connect existing logic with new UI

## 2. JavaFX GUI Implementation

### 2.1. Basic Application Framework
- Create main `KüberSimulaatorFX` class extending JavaFX `Application`
- Implement simple scene management system
- Set up basic application lifecycle (start method)

### 2.2. UI Screens Development

#### 2.2.1. Player Selection Screen
- Simple design with list of player profiles
- "Create New Player" button with text input dialog
- Exit button
- Basic styling for readability

#### 2.2.2. Main Menu Screen
- Show selected player name
- Navigation buttons:
  - Play Random Scenario
  - Select Category
  - View Statistics
  - Instructions
  - Back to Player Selection
  - Exit Application
- Simple layout with consistent spacing

#### 2.2.3. Category Selection Screen
- List of available categories
- Selection and back buttons
- Basic transitions between screens

#### 2.2.4. Scenario Screen
- Area for scenario title and description
- Question display area
- Radio buttons for answer options
- Continue button
- Simple collapsible information panel
- Exit scenario button

#### 2.2.5. Results Screen
- Score display
- Educational summary from existing implementation
- Navigation buttons:
  - Play Again
  - Select New Scenario
  - Return to Main Menu

#### 2.2.6. Statistics Screen
- Simple statistics display:
  - Total score
  - Completed scenarios count
  - Failed/exited scenarios count
  - Basic chart for category performance

### 2.3. Basic Styling
- Create simple CSS theme for consistent appearance:
  - Basic color scheme
  - Readable fonts
  - Consistent spacing
- Add basic transitions between scenes

## 3. File Operations Implementation

### 3.1. Simple Directory Structure
- Create basic directory structure for application data:
```
data/
├── players/
│   ├── player1/
│   │   ├── profile.txt
│   │   └── log.txt
│   └── player2/
│       ├── profile.txt
│       └── log.txt
```

### 3.2. Player Profile Management
- Create `KasutajaProfiil` class with basic information:
  - Player name
  - Total score
  - Completed scenarios with scores
  - Failed/exited scenarios
- Implement simple methods for:
  - Loading profiles from text files
  - Saving profiles to text files
  - Creating new profiles

### 3.3. Basic Logging System
- Create `LogiHaldur` class for simple logging:
  - Session start/end times
  - User choices
  - Scores
- Store logs in simple text format
- Implement basic log reading functionality

## 4. Basic Exception Handling

### 4.1. File Operation Exceptions
- Implement try-catch blocks for file operations
- Display simple error dialogs when file operations fail
- Add fallback mechanisms for missing files

### 4.2. Input Validation
- Validate user input in text fields
- Handle empty or invalid selections
- Prevent application crashes from user errors

## 5. Basic Event Handling

### 5.1. Mouse Events
- Implement button click handlers
- Add basic hover effects for buttons
- Handle selection events in lists

### 5.2. Keyboard Support
- Add Enter key support for primary actions
- Implement Escape key for cancel/back actions
- Add Tab navigation between UI elements

## 6. Simple Responsive Design

### 6.1. Basic Layout Strategies
- Use JavaFX's layout containers:
  - BorderPane for main layout
  - VBox and HBox for content organization
- Set minimum window size constraints

### 6.2. Simple Scaling
- Use percentage-based sizing for main components
- Implement ScrollPane for content that might not fit
- Test at different window sizes to ensure usability

## 7. Additional Features (Optional)

### 7.1. Sound Effects
- Simple sound effects for interactions (if time permits)
- Basic volume control

### 7.2. Visual Improvements
- Basic animations for transitions
- Simple visual feedback for actions

## 8. Implementation Sequence

### Phase 1: Core Framework
- Set up JavaFX application framework
- Create basic scene structure
- Implement navigation between screens

### Phase 2: Basic UI
- Implement all required screens with minimal styling
- Set up event handlers for basic interaction
- Test basic flow through the application

### Phase 3: File Operations
- Implement player profile system
- Add logging functionality
- Test file reading and writing

### Phase 4: Finalization
- Add responsive layout adjustments
- Implement exception handling
- Final testing and bug fixing

## 9. Testing Approach

### 9.1. Functional Testing
- Test each screen and feature manually
- Verify all requirements are met
- Check file operations

### 9.2. UI Testing
- Test at different window sizes
- Verify keyboard and mouse interactions
- Check exception handling

## 10. Documentation

### 10.1. Code Documentation
- Add JavaDoc comments for new classes and methods
- Update existing documentation as needed

### 10.2. User Instructions
- Add in-app instructions
- Update README if needed
