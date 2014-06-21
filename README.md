#SpeakEasy

SpeakEasy is a collection of communication libraries.  SpeakEasy includes:

* A number of protocol stacks with a consistent metaphor
* Infrastructure to implement the protocol stacks
* Higher-level client and server APIs to simplify the consumption and use of the protocol stacks

SpeakEasy is a work in progress and is not yet ready for use in anything real.

SpeakEasy is licensed via the [MIT License](http://opensource.org/licenses/MIT).


###Subprojects

* *core* - Common infrastructure internal to SpeakEasy.  Not intended for use outside of SpeakEasy.


###Branches
The branches in SpeakEasy should look like this:

```asciidoc
master  dev  (feature/user branch)
  |
  +----->|
  |      |
  |      +----->|
  |      |      |
  |      | pull |
  |      |<-----+
  | pull |
  |<-----+
```

Points of note:

* master is only ever updated via pull request from dev only.
* No other branches should ever be created off of master.
* dev is only ever updated via pull request from feature or user branches.  (Some exceptions can be made for simple changes like editing README files.)
* Feature or user branches may be made from dev or from other feature or user branches.
* Integration, verification, stability all happen on the dev branch.

How committer code changes get into master:

* Make code changes to a feature or user branch.
* Issue a pull request into dev.
* After integration, verification, and stability, pull request from dev to master.
