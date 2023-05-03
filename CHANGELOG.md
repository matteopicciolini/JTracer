# Fix bug 
## Vertical coordinates flipped upside-down

The bug we've fixed used to fip vertical cooordinates due to a sign error in the formula. 

` u = (col + u_pixel) / (self.image.width - 1) `
` v = (row + v_pixel) / (self.image.height - 1) `