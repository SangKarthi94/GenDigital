import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sangavi.gendigital.ui.post.viewmodel.FilterOption
import com.sangavi.gendigital.ui.post.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    viewModel: PostViewModel,
    onDismiss: () -> Unit,
    modalBottomSheetState: SheetState
) {
    val filterOption = viewModel.selectedFilter.collectAsState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(text = "Filter Options")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.setFilter(FilterOption.ALL_POSTS) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = filterOption.value == FilterOption.ALL_POSTS,
                    onClick = { viewModel.setFilter(FilterOption.ALL_POSTS) }
                )
                Text(text = "All Posts", modifier = Modifier.padding(start = 8.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.setFilter(FilterOption.MY_POSTS) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = filterOption.value == FilterOption.MY_POSTS,
                    onClick = { viewModel.setFilter(FilterOption.MY_POSTS) }
                )
                Text(text = "My Posts", modifier = Modifier.padding(start = 8.dp))
            }
            Button(onClick = onDismiss, modifier = Modifier.padding(top = 16.dp)) {
                Text(text = "Done")
            }
        }
    }
}