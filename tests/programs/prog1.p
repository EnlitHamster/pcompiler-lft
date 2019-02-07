read(choice);

while (choice > 0) {
	read(a);
	read(b);

	case
		when (choice == 1) r := a + b
		when (choice == 2) r := a - b
		when (choice == 3) r := a * b
		when (choice == 4) r := a / b
	else r := 0 - 1;

	print(r);
	read(choice)
}
