awk '
/val prev = / { 
  print "                        if (games.isEmpty()) {"
  print "                            Text(\"NO GAMES INSTALLED\", color = TextSecondary, fontFamily = Rajdhani, fontSize = 2.cqhSp(m))"
  print "                        } else {"
}
/Spacer\(modifier = Modifier\.height\(1\.0\.cqh\(m\)\)\)/ {
  if (braceCount == 0) {
    foundEmptyBox = 1
  }
}
{
  if (foundEmptyBox == 1 && /Box\(/) {
      braceCount = 1
  }
  
  print $0
  
  if (foundEmptyBox == 1 && braceCount > 0) {
      if (/{/) braceCount++
      if (/}/) braceCount--
      if (braceCount == 0) {
          print "                        }"
          foundEmptyBox = 0
      }
  }
}
' app/src/main/java/com/example/ui/screens/DashboardScreen.kt > tmp.kt
mv tmp.kt app/src/main/java/com/example/ui/screens/DashboardScreen.kt
