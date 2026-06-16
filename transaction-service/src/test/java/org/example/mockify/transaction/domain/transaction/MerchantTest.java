package org.example.mockify.transaction.domain.transaction;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MerchantTest {

    @Test
    void should_be_equal_when_both_fields_match() {
        Merchant a = new Merchant("M001", "Spotify");
        Merchant b = new Merchant("M001", "Spotify");
        assertThat(a).isEqualTo(b);
    }

    @Test
    void should_not_be_equal_when_merchant_id_differs() {
        Merchant a = new Merchant("M001", "Spotify");
        Merchant b = new Merchant("M002", "Spotify");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void should_not_be_equal_when_merchant_name_differs() {
        Merchant a = new Merchant("M001", "Spotify");
        Merchant b = new Merchant("M001", "Apple Music");
        assertThat(a).isNotEqualTo(b);
    }
}
