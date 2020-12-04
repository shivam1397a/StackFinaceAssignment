Feature: Admin

  Scenario Outline: Verify on entering <User>, user can <Results> admin
    Given I navigate to Library System home page in "chrome" browser
    And I click "Sign-In as Admin" button
    When I login to Library Systems as "<User>"
    Then I verify that user is "<Results>"
    And I Logout of the application
    Examples: Users and access
      | User        | Results                |
      | Admin creds | successfully logged in |
      | User creds  | not logged in          |

  Scenario: Verify admin access for managing book records
    Given I navigate to Library System home page in "chrome" browser
    And I click "Sign-In as Admin" button
    And I login to Library Systems as "Admin creds"
    And I click on "Manage Books" tab
    And I click "Add New book" button
    When I click "Add" button
    Then I validate "Please fill out this field." error message is present
    When I provide values for new book record
    And I click "Add" button
    Then I verify that record is successfully created
    When I click "Edit" button for above created book record
    Then I verify that "Available for issue,Available for read" options are available
    And I click "Edit" button
    Then I verify that record is successfully created
    When I click "Delete" button for above created book record
    Then I verify that record is successfully deleted
    And I Logout of the application
