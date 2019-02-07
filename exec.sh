#!/bin/sh

# Error: number of arguments not acceptable
if [[ "$#" -ne 2 && "$#" -ne 3 ]]; then

	echo "Too few/many arguments."
	echo "Usage: ./exec.sh <build|test> <program> [f = rebuild]"

elif [ "$1" = "build" ]; then

	# Generating the needed data
	source ./map.sh
	map_dir "$2"
	map_make "$2" "$dir"
	map_exec "$2" "$dir"
	map_clr "$2" "$dir"

	# If the file is recognised
	if [[ "$dir" != "-1" && "$mkcall" != "-1" && "$exec" != "-1" && "$clr" != "-1" ]]; then

		# Force the build
		if [[ "$3" = "f" ]]; then
			eval "$clr"
		fi

		# Building and executing
		eval "make $mkcall"
		cd bin
		eval "$exec"

	else
		echo "File unrecognised."
	fi

elif [ "$1" = "test" ]; then

	# Generating the needed data
	source ./map.sh
	map_dir "$2"
	map_exec "$2" "$dir"

	if [[ "$dir" != "-1" && "$exec" != "-1" ]]; then
		cd bin

		# Executing all available tests
		echo "============================================="
		while read line; do
			echo
			cmd="$exec $line"
			eval $cmd
			echo
			echo "============================================="
		done < "../tests/${2}.test"
	else
		echo "File unrecognised."
	fi

# Error: argument #1 not recognised
else

	echo "Wrong arguments."
	echo "Usage: ./exec.sh <build|test> <program> [f = rebuild]"

fi
