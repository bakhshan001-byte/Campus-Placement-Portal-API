
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginData) {
        String email = loginData.getEmail() == null ? "" : loginData.getEmail().trim();
        User existingUser = userRepository.findByEmail(email);

        if (existingUser == null) {
            return ResponseEntity.badRequest().body("DEBUG: user not found");
        }

        boolean isPasswordMatch = passwordEncoder.matches(loginData.getPassword(), existingUser.getPassword());

        if (!isPasswordMatch) {
            return ResponseEntity.badRequest().body("DEBUG: wrong password");
        }

        String token = jwtUtil.generateToken(existingUser.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", existingUser);

        return ResponseEntity.ok(response);
    }
