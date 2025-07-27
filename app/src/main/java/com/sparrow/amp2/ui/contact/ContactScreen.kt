package com.sparrow.amp2.ui.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ContactScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Contact Us",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Garden Blossom Store",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Contact Information Cards
        ContactInfoCard(
            icon = Icons.Default.Phone,
            title = "Phone",
            info = "+91 9876543210"
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ContactInfoCard(
            icon = Icons.Default.Email,
            title = "Email",
            info = "contact@gardenblossom.store"
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ContactInfoCard(
            icon = Icons.Default.LocationOn,
            title = "Address",
            info = "123 Garden Street\nBlossom City, GC 123456\nIndia"
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ContactInfoCard(
            icon = Icons.Default.DateRange,
            title = "Business Hours",
            info = "Monday - Saturday: 9:00 AM - 7:00 PM\nSunday: 10:00 AM - 5:00 PM"
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "We're here to help you grow your garden! Feel free to reach out with any questions about our plants, care instructions, or orders.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun ContactInfoCard(
    icon: ImageVector,
    title: String,
    info: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = info,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
