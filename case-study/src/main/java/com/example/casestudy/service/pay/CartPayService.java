//package com.example.casestudy.service;
//
//import com.example.casestudy.model.CartItem;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//    @Service
//    public class CartService {
//        private List<CartItem> cartItems = new ArrayList<>();
//        private List<Long> selectedItemIds = new ArrayList<>();
//
//        public void addItem(CartItem item) {
//            cartItems.add(item);
//        }
//
//        public List<CartItem> getAllItems() {
//            return cartItems;
//        }
//
//        public void setSelectedItems(List<Long> itemIds) {
//            this.selectedItemIds = itemIds;
//        }
//
//        public List<CartItem> getSelectedItems() {
//            return cartItems.stream()
//                    .filter(item -> selectedItemIds.contains(item.getId()))
//                    .collect(Collectors.toList());
//        }
//}