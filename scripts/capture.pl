#!/usr/bin/env perl
use v5.20;
use utf8;
use strict;
use warnings;
use autodie;
use Getopt::Long 'HelpMessage';

# First block to replay
my $start;
# Last block to replay
my $end;
# Shadow node hostnamt
my $shadownode_fqdn = 'ec2-107-21-85-50.compute-1.amazonaws.com'; # default value
# Maybe the user needs a specific ssh key
my $ssh_key = '';
# Destination folder
my $destination = 'arithmetization/src/test/resources/replays';
# Destination filename
my $filename;

GetOptions(
    'help' => sub {HelpMessage(0)},
    'start=o' => \$start,
    'end:o' => \$end,
    'server:s' => \$shadownode_fqdn,
    'destination:s' => \$destination,
    'ssh-key:s' => sub {
        shift; # drop the first arg, i.e. "ssh-key"
        $ssh_key = " -i " . shift . " ";
    }) or HelpMessage(1);
HelpMessage(1) unless $start; # $start is mandatory


if ($end) {
    say "Capturing conflation $start - $end";
    $filename = "$destination/$start-$end.json.gz";
} else {
    say "Capturing block $start";
    $end = $start;
    $filename = "$destination/$start.json.gz";
}
say "Writing replay to `$filename`";


my $payload = "{
   \\\"jsonrpc\\\":\\\"2.0\\\",
   \\\"method\\\":\\\"rollup_captureConflation\\\",
   \\\"params\\\":[\\\"$start\\\", \\\"$end\\\"], \\\"id\\\":\\\"1\\\"
}";

my $cmd = qq(
ssh ${ssh_key} ec2-user\@${shadownode_fqdn} -C "
curl -X POST 'http://localhost:8545' --data '$payload' |
jq '.result.capture' -r | jq . | gzip
");


open (my $file, '>', $filename) or die "Could not open file: $!";
print $file qx/$cmd/;
close $file;


=head1 NAME

capture - capture conflation replays from a shadow node

=head1 SYNOPSIS

  --start         The first block to replay (required)
  --end           The last block to replay (default to <start>)
  --ssh-key       If applicable, path the SSH key to use to connect to the shadow node
  --server        The shadow node hostname (defaults to `ec2-107-21-85-50.compute-1.amazonaws.com`)
  --destination   Where to write the replay file (defaults to `arithmetization/src/test/resources/replays`)
  --help, -h      Print this message

=head1 VERSION

1.0

=cut
