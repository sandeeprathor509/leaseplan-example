Feature: Search for the product

  Scenario Outline: Verify the response when user calls the api for valid product
    Given the request with the given product "<product>"
    Then validate the processed "valid" request

    Examples:
    |product|
    |apple  |
    |mango  |
    |tofu   |
    |water  |

  Scenario Outline: Verify the response when user calls the api for invalid product
    Given the request with the given product "<product>"
    Then validate the processed "invalid" request

    Examples:
      |product|
      |car    |
      |bike   |
      |engine |

  Scenario: Verify the response when the user calls the api for null data
    Given the request with the given product " "
    Then validate the processed "Not authenticated" request

  Scenario: Verify the response when api link is not found
    Given the requested url does not exist
    Then validate the response as url not found