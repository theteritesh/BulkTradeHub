package com.technoworld.BulkTradeHub.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.technoworld.BulkTradeHub.entity.RetailShopProfile;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.ProfileRepository;
import com.technoworld.BulkTradeHub.service.RetailShopProfileService;

@Controller
@RequestMapping("/retail")
public class RetailShopProfileController {

    @Autowired
    private RetailShopProfileService retailShopProfileService;
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @GetMapping("/profile")
    public String getProfilePage(Model model,Principal principal) {
        User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        
        RetailShopProfile retailShopProfile=(RetailShopProfile)profileRepository.findByUser(user); 
        
        if (retailShopProfile == null) {
            retailShopProfile = new RetailShopProfile();
        } else {
            if (retailShopProfile.getPaymentMethods() != null && !retailShopProfile.getPaymentMethods().isEmpty()) {
            	retailShopProfile.setPaymentMethodsList(Arrays.asList(retailShopProfile.getPaymentMethods().split(",")));
            }
            retailShopProfile.setProfileImg(retailShopProfile.getProfileImg());
        }
        
        model.addAttribute("retailShopProfile", retailShopProfile);
        return "retailshop/profile";
    }
    
    @GetMapping("/profileImg")
    @ResponseBody
    public ResponseEntity<byte[]> getProfileImage(Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        RetailShopProfile retailShopProfile = (RetailShopProfile) profileRepository.findByUser(user);

        if (retailShopProfile != null && retailShopProfile.getProfileImg() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(retailShopProfile.getProfileImg());
        }

        try {
            InputStream defaultImageStream = getClass().getResourceAsStream("/static/retailshop/img/default.png");
            if (defaultImageStream != null) {
                byte[] defaultImageBytes = defaultImageStream.readAllBytes();
                return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(defaultImageBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.notFound().build();
    }



    @PostMapping("/profile/save")
    public String saveProfile(
            @RequestParam String shopName,
            @RequestParam String shopType,
            @RequestParam String gstNumber,
            @RequestParam String phoneNumber,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam String country,
            @RequestParam String pincode,
            @RequestParam(required = false) String bankAccountHolderName,
            @RequestParam(required = false) String bankAccountNumber,
            @RequestParam(required = false) String bankName,
            @RequestParam(required = false) String ifscCode,
            @RequestParam(required = false) String upiId,
            @RequestParam(required = false) List<String> paymentMethodsList,
            @RequestParam(required = false) MultipartFile profileImg,
            Principal principal) throws IOException {
    	
    	User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    	RetailShopProfile retailShopProfile=(RetailShopProfile) profileRepository.findByUser(user);
        
        if (retailShopProfile == null) {
        	retailShopProfile = new RetailShopProfile();
        	retailShopProfile.setUser(user);
        }
        
        retailShopProfile.setShopName(shopName);
        retailShopProfile.setShopType(shopType);
        retailShopProfile.setGstNumber(gstNumber);
        retailShopProfile.setPhoneNumber(phoneNumber);
        retailShopProfile.setAddress(address);
        retailShopProfile.setCity(city);
        retailShopProfile.setState(state);
        retailShopProfile.setCountry(country);
        retailShopProfile.setPincode(pincode);
        retailShopProfile.setBankAccountHolderName(bankAccountHolderName);
        retailShopProfile.setBankAccountNumber(bankAccountNumber);
        retailShopProfile.setBankName(bankName);
        retailShopProfile.setIfscCode(ifscCode);
        retailShopProfile.setUpiId(upiId);
        retailShopProfile.setPaymentMethods(String.join(",", paymentMethodsList));
        
        System.out.println("Profile Img"+profileImg.getBytes());

        if(profileImg !=null && !profileImg.isEmpty()) {
        	retailShopProfile.setProfileImg(profileImg.getBytes());
        }

        retailShopProfileService.saveProfile(retailShopProfile);

        return "redirect:/retail/profile?success";
    }

}
