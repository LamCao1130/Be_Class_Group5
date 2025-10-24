package com.he181464.be_class.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwoFAData {
    private String secret;
    private String qrCodeBase64;
    private String otpAuthUrl;
}
